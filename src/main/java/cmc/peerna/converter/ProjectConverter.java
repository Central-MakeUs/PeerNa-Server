package cmc.peerna.converter;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.Project;
import cmc.peerna.service.MemberService;
import cmc.peerna.service.ProjectService;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import cmc.peerna.web.dto.responseDto.ProjectResponseDto;
import cmc.peerna.web.dto.responseDto.RootResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ProjectConverter {
    public static ProjectResponseDto.ProjectDetailDto toProjectDetailDto(Project project) {
        return ProjectResponseDto.ProjectDetailDto.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .introduce(project.getIntroduce())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .openChattingLink(project.getOpenChattingLink())
                .notionLink(project.getNotionLink())
                .githubLink(project.getGithubLink())
                .creatorSimpleProfileDto(MemberConverter.toMemberSimpleProfileDto(project.getCreator()))
                .build();
    }

    public static ProjectResponseDto.ProjectSimpleProfileDto toProjectSimpleProfile(Project project) {
        return ProjectResponseDto.ProjectSimpleProfileDto.builder()
                .projectId(project.getId())
                .projectName(project.getName())
                .introduce(project.getIntroduce())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .build();
    }

    public static List<ProjectResponseDto.ProjectSimpleProfileDto> toProjectSimpleProfileList(List<Project> projectList) {
        if(projectList.isEmpty()) return null;
        List<ProjectResponseDto.ProjectSimpleProfileDto> projectSimpleProfileDtoList = projectList.stream()
                .map(project -> toProjectSimpleProfile(project))
                .collect(Collectors.toList());
        return projectSimpleProfileDtoList;
    }


    public static ProjectResponseDto.ProjectPageDto toProjectPageDto(Page<Project> projectPage) {
//        if(projectPage.getTotalElements()==0L) return null;
        List<ProjectResponseDto.ProjectSimpleProfileDto> projectSimpleProfileDtoList = projectPage.stream()
                .map(project -> toProjectSimpleProfile(project))
                .collect(Collectors.toList());

        return ProjectResponseDto.ProjectPageDto.builder()
                .projectList(projectSimpleProfileDtoList)
                .isFirst(projectPage.isFirst())
                .isLast(projectPage.isLast())
                .totalPage(projectPage.getTotalPages())
                .totalElements(projectPage.getTotalElements())
                .currentPageElements(projectPage.getNumberOfElements())
                .build();
    }
}
