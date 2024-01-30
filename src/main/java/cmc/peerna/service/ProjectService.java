package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.web.dto.requestDto.ProjectRequestDto;

public interface ProjectService {
    void newProject(Member member, ProjectRequestDto.ProjectCreateDto request);
}
