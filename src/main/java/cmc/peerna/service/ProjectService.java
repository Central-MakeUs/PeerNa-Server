package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.Project;
import cmc.peerna.web.dto.requestDto.ProjectRequestDto;
import cmc.peerna.web.dto.responseDto.ProjectResponseDto;

public interface ProjectService {
    void newProject(Member member, ProjectRequestDto.ProjectCreateDto request);
    Project findById(Long projectId);

    ProjectResponseDto.ProjectDetailDto getProjectDetailInfo(Long projectId);

    ProjectResponseDto.ProjectPageDto getAllProject(Integer page);
    ProjectResponseDto.ProjectPageDto getMyProject(Member member, Integer page);

    String findProjectCreator(Long projectId);

    void checkExistProjectMember(Long projectId, Long memberId);

    void checkProjectSelfInvite(Long projectId, Long memberId);

    void saveNewProjectMember(Long projectId, Long memberId);
}
