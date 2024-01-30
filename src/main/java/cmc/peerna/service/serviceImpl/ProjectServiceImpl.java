package cmc.peerna.service.serviceImpl;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.Project;
import cmc.peerna.repository.ProjectRepository;
import cmc.peerna.service.ProjectService;
import cmc.peerna.web.dto.requestDto.ProjectRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

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
}
