package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.jwt.handler.annotation.AuthMember;
import cmc.peerna.service.ProjectService;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.requestDto.ProjectRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "2000", description = "OK 성공"),
        @ApiResponse(responseCode = "4007", description = "feign에서 400번대 에러가 발생했습니다. 코드값이 잘못되었거나 이미 해당 코드를 통해 토큰 요청을 한 경우.\""),
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
}
