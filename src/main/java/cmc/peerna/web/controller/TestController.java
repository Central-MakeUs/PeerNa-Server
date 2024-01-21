package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.jwt.handler.annotation.AuthMember;
import cmc.peerna.service.MemberService;
import cmc.peerna.service.TestService;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.requestDto.TestRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import cmc.peerna.web.dto.responseDto.TestResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
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
@Tag(name = "셀프테스트, 피어테스트 관련 API 목록", description = "셀프테스트, 피어테스트 관련 API 목록입니다.")

public class TestController {

    private final MemberService memberService;
    private final TestService testService;

    @Operation(summary = "셀프 테스트 API ✔️🔑", description = "셀프 테스트 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "4200", description = "BAD_REQUEST, 잘못된 답변 ID 값을 전달했습니다."),
            @ApiResponse(responseCode = "4201", description = "BAD_REQUEST, 답변 개수가 정확하게 18개가 아닙니다.")
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PostMapping("/member/selfTest")
    public ResponseDto<TestResponseDto.selfTestResultResponseDto> saveSelfTest(@AuthMember Member member, @RequestBody MemberRequestDto.selfTestDto request) {

        testService.deleteSelfTestResult(member);
        testService.saveSelfTest(member, request);
        TestResponseDto.selfTestResultResponseDto selfTestResult = testService.saveAndGetSelfTestResult(member);
        return ResponseDto.of(selfTestResult);
    }

    @Operation(summary = "셀프 테스트 삭제 API ✔️🔑", description = "셀프 테스트 삭제 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저입니다."),
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @DeleteMapping("/member/selfTest")
    public ResponseDto<MemberResponseDto.MemberStatusDto> deleteSelfTest(@AuthMember Member member) {
        testService.deleteSelfTest(member);
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "DeleteSelfTest"));
    }


    @Operation(summary = "셀프 테스트 결과 조회 API ✔️🔑", description = "셀프 테스트 결과 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저입니다."),
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/member/selfTestResult")
    public ResponseDto<TestResponseDto.selfTestResultResponseDto> getSelfTestResult(@AuthMember Member member) {

        TestResponseDto.selfTestResultResponseDto selfTestResult = testService.getSelfTestResult(member);
        return ResponseDto.of(selfTestResult);
    }

    @Operation(summary = "비회원 피어 테스트 작성 API ✔️", description = "비회원 피어 테스트 작성 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "4200", description = "BAD_REQUEST, 잘못된 답변 ID 값을 전달했습니다."),
            @ApiResponse(responseCode = "4201", description = "BAD_REQUEST, 답변 개수가 정확하게 18개가 아닙니다.")
    })
    @PostMapping("/review/peerTest/{targetId}") // /UUID 붙여서 target 식별 로직  추가하기 , 임시로 requestParam으로.
    public ResponseDto<TestResponseDto.peerTestIdResponseDto> savePeerTest(@RequestParam(name = "targetId")Long targetId,  @RequestBody TestRequestDto.peerTestRequestDto requestDto) {
//        Member writer = Member.builder()
//                .id(0L).build();
        testService.savePeerTest(null, memberService.findById(targetId), requestDto);
        return ResponseDto.of(TestResponseDto.peerTestIdResponseDto.builder()
                .peerTestId(targetId).build());
    }


    @Operation(summary = "비회원 유저 회원가입 후 id값 갱신용 API ✔️🔑", description = "비회원 유저 회원가입 후 id값 갱신용 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저입니다.")
    })
    @PostMapping("/review/updateMemberId")
    public ResponseDto<MemberResponseDto.MemberStatusDto> updateMemberId(@AuthMember Member member, @RequestBody String uuid) {
        testService.updatePeerTestMemberId(member, uuid);
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "UpdatePeerTestWriterId"));
    }

}
