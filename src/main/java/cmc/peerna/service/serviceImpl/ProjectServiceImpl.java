package cmc.peerna.service.serviceImpl;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.apiResponse.exception.handler.ProjectException;
import cmc.peerna.apiResponse.exception.handler.RootException;
import cmc.peerna.converter.ProjectConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.Project;
import cmc.peerna.domain.mapping.ProjectMember;
import cmc.peerna.repository.MemberRepository;
import cmc.peerna.repository.ProjectMemberRepository;
import cmc.peerna.repository.ProjectRepository;
import cmc.peerna.service.ProjectService;
import cmc.peerna.web.dto.requestDto.ProjectRequestDto;
import cmc.peerna.web.dto.responseDto.ProjectResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;
    @Value("${paging.size}")
    private Integer pageSize;

    @Override
    @Transactional
    public void newProject(Member member, ProjectRequestDto.ProjectCreateDto request) {
        Project project = Project.builder()
                .name(request.getProjectName())
                .creator(member)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .introduce(request.getIntroduce())
                .openChattingLink(request.getOpenChattingLink())
                .notionLink(request.getNotionLink())
                .githubLink(request.getGithubLink())
                .build();
        projectRepository.save(project);
    }

    @Override
    public Project findById(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectException(ResponseStatus.PROJECT_NOT_FOUND));
        return project;

    }
    @Override
    public ProjectResponseDto.ProjectDetailDto getProjectDetailInfo(Long projectId) {
        Project project = findById(projectId);
        return ProjectConverter.toProjectDetailDto(project);
    }

    @Override
    public ProjectResponseDto.ProjectPageDto getAllProject(Integer page) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("startDate"), Sort.Order.asc("name")));

        Page<Project> allProjectPage = projectRepository.findAllByIdNotNull(pageRequest);
        if (allProjectPage.getTotalElements() == 0L) {
            throw new RootException(ResponseStatus.PROJECT_COUNT_ZERO);
        }
        if (allProjectPage.getTotalPages() <= page)
            throw new MemberException(ResponseStatus.OVER_PAGE_INDEX_ERROR);

        ProjectResponseDto.ProjectPageDto allProjectPageDto = ProjectConverter.toProjectPageDto(allProjectPage);
        return allProjectPageDto;
    }

    @Override
    public ProjectResponseDto.ProjectPageDto getMyProject(Member member, Integer page) {
        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("startDate"), Sort.Order.asc("name")));

        Page<Project> allProjectPage = projectRepository.findAllByCreator(member, pageRequest);
        if (allProjectPage.getTotalElements() == 0L) {
            throw new RootException(ResponseStatus.PROJECT_COUNT_ZERO);
        }
        if (allProjectPage.getTotalPages() <= page)
            throw new MemberException(ResponseStatus.OVER_PAGE_INDEX_ERROR);

        ProjectResponseDto.ProjectPageDto allProjectPageDto = ProjectConverter.toProjectPageDto(allProjectPage);
        return allProjectPageDto;
    }

    @Override
    public String findProjectCreator(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectException(ResponseStatus.PROJECT_NOT_FOUND));
        return project.getCreator().getName();

    }

    @Override
    public void checkExistProjectMember(Long projectId, Long memberId) {
        if (projectMemberRepository.existsByProjectIdAndMemberId(projectId, memberId)) {
            throw new ProjectException(ResponseStatus.ALREADY_EXIST_PROJECT_MEMBER);
        }

    }

    @Override
    public void checkProjectSelfInvite(Long projectId, Long memberId){
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectException(ResponseStatus.PROJECT_NOT_FOUND));
        if (project.getCreator().getId() == memberId) {
            throw new ProjectException(ResponseStatus.PROJECT_SELF_INVITE);
        }
    }

    @Override
    public void saveNewProjectMember(Long projectId, Long memberId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectException(ResponseStatus.PROJECT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ResponseStatus.MEMBER_NOT_FOUND));
        projectMemberRepository.save(ProjectMember.builder()
                .project(project)
                .member(member)
                .build()
        );
    }


}
