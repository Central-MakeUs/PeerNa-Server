package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.jwt.handler.annotation.AuthMember;
import cmc.peerna.service.ProjectService;
import cmc.peerna.validation.annotation.CheckPage;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.requestDto.ProjectRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import cmc.peerna.web.dto.responseDto.ProjectResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "2000", description = "OK 성공"),
        @ApiResponse(responseCode = "4008", description = "토큰이 올바르지 않습니다."),
        @ApiResponse(responseCode = "4009", description = "리프레쉬 토큰이 유효하지 않습니다. 다시 로그인 해주세요"),
        @ApiResponse(responseCode = "4010", description = "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
        @ApiResponse(responseCode = "4011", description = "모든 토큰이 만료되었습니다. 다시 로그인해주세요."),
        @ApiResponse(responseCode = "5000", description = "서버 에러, 로빈에게 알려주세요."),
})
@Tag(name = "Project 관련 API 목록", description = "Project 관련 API 목록입니다.")

public class ProjectController {
    private final ProjectService projectService;

    @Operation(summary = "프로젝트 생성 API ✔️🔑", description = "새 프로젝트 생성하는 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PostMapping("/project")
    public ResponseDto<MemberResponseDto.MemberStatusDto> saveMemberInfo(@AuthMember Member member, @RequestBody @Valid ProjectRequestDto.ProjectCreateDto request) {
        projectService.newProject(member, request);
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "프로젝트 생성 완료"));
    }

    @Operation(summary = "전체 프로젝트 조회 API ✔️🔑", description = "전체 프로젝트 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "4012", description = "BAD_REQUEST , 페이지 번호는 1 이상이여야 합니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST , 페이지 번호가 페이징 범위를 초과했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2301", description = "OK , 프로젝트가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)

    })
    @GetMapping("/project")
    public ResponseDto<ProjectResponseDto.ProjectPageDto> getAllProject(@AuthMember Member member, @CheckPage @RequestParam(name = "page") Integer page) {
        if (page == null)
            page = 1;
        else if (page < 1)
            throw new MemberException(ResponseStatus.UNDER_PAGE_INDEX_ERROR);
        page -= 1;

        ProjectResponseDto.ProjectPageDto allProject = projectService.getAllProject(page);
        return ResponseDto.of(allProject);
    }

    @Operation(summary = "내 프로젝트 조회 API ✔️🔑", description = "내 프로젝트 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "4012", description = "BAD_REQUEST , 페이지 번호는 1 이상이여야 합니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST , 페이지 번호가 페이징 범위를 초과했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "2301", description = "OK , 프로젝트가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/project/my")
    public ResponseDto<ProjectResponseDto.ProjectPageDto> getMyProject(@AuthMember Member member, @CheckPage @RequestParam(name = "page") Integer page) {
        if (page == null)
            page = 1;
        else if (page < 1)
            throw new MemberException(ResponseStatus.UNDER_PAGE_INDEX_ERROR);
        page -= 1;

        ProjectResponseDto.ProjectPageDto allProject = projectService.getMyProject(member, page);
        return ResponseDto.of(allProject);
    }

    @Operation(summary = "프로젝트 상세 페이지 조회 API ✔️🔑", description = "프로젝트 상세 페이지 조회 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/project/{project-id}")
    public ResponseDto<ProjectResponseDto.ProjectDetailDto> getProjectDetail(@AuthMember Member member, @PathVariable(name = "project-id") Long projectId) {
        ProjectResponseDto.ProjectDetailDto projectDetailInfo = projectService.getProjectDetailInfo(projectId);
        return ResponseDto.of(projectDetailInfo);
    }
}
