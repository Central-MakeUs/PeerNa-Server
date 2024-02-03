package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.NoticeGroup;
import cmc.peerna.domain.enums.NoticeType;
import cmc.peerna.fcm.service.FcmService;
import cmc.peerna.jwt.handler.annotation.AuthMember;
import cmc.peerna.service.MemberService;
import cmc.peerna.service.NoticeService;
import cmc.peerna.service.TestService;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.requestDto.TestRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import cmc.peerna.web.dto.responseDto.TestResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


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
    private final FcmService fcmService;
    private final NoticeService noticeService;

    private final String fcmTitle = "[PeerNa]";


    @Operation(summary = "셀프 테스트 API ✔️🔑", description = "셀프 테스트 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "4200", description = "BAD_REQUEST, 잘못된 답변 ID 값을 전달했습니다."),
            @ApiResponse(responseCode = "4201", description = "BAD_REQUEST, 답변 개수가 정확하게 18개가 아닙니다.")
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PostMapping("/member/self-test")
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
    @DeleteMapping("/member/self-test")
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
    @GetMapping("/member/self-test-result")
    public ResponseDto<TestResponseDto.selfTestResultResponseDto> getSelfTestResult(@AuthMember Member member) {

        TestResponseDto.selfTestResultResponseDto selfTestResult = testService.getSelfTestResult(member);
        return ResponseDto.of(selfTestResult);
    }

    @Operation(summary = "비회원 피어 테스트 작성 API ✔️", description = "비회원 피어 테스트 작성 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2250", description = "BAD_REQUEST, 잘못된 UUID 값입니다."),
            @ApiResponse(responseCode = "4200", description = "BAD_REQUEST, 잘못된 답변 ID 값을 전달했습니다."),
            @ApiResponse(responseCode = "4201", description = "BAD_REQUEST, 답변 개수가 정확하게 18개가 아닙니다.")
    })
    @PostMapping("/review/peer-test")
    public ResponseDto<TestResponseDto.peerTestIdResponseDto> savePeerTest(@RequestParam(name = "target-uuid") String targetUuid,  @RequestBody TestRequestDto.peerTestRequestDto requestDto) {
        Member target = memberService.findMemberByUuid(targetUuid);
        testService.savePeerTest(null, target, requestDto);
        memberService.updateTotalScore(target);
        memberService.updatePeerTestType(target);
        return ResponseDto.of(TestResponseDto.peerTestIdResponseDto.builder()
                .peerTestId(target.getId()).build());
    }

    @Operation(summary = "비회원 유저 회원가입 후 id값 갱신용 API ✔️🔑", description = "비회원 유저 회원가입 후 id값 갱신용 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "2250", description = "BAD_REQUEST, 해당 UUID로 작성된 정보가 존재하지 않습니다. 잘못된 UUID 값 입니다."),
    })
    @PostMapping("/review/update-member-id")
    public ResponseDto<MemberResponseDto.MemberStatusDto> updateMemberId(@AuthMember Member member, @RequestBody MemberRequestDto.uuidRequestDto uuid) {
        testService.updatePeerTestMemberId(member, uuid.getUuid());
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "UpdatePeerTestWriterId"));
    }


    @Operation(summary = "[동료 상세 페이지] 내 피어 테스트 응답 요청하기 API ✔️🔑", description = "[동료 상세 페이지] 내 피어 테스트 응답 요청하기 API API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저입니다."),
            @ApiResponse(responseCode = "2251", description = "OK, 이미 피어테스트를 진행했습니다."),
            @ApiResponse(responseCode = "2351", description = "OK , 해당 유저의 Fcm Token이 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @PostMapping("/review/request/{peer-id}")
    public ResponseDto<MemberResponseDto.MemberStatusDto> requestPeerTest(@AuthMember Member member, @PathVariable(name = "peer-id") Long peerId) {

        testService.checkExistPeerTest(peerId, member.getId());

        String messageContents = member.getName()+"님이 피어테스트 응답을 요청했어요";
        noticeService.createNotice(member, peerId, NoticeGroup.PEER_TEST, NoticeType.PEER_TEST_REQUEST, member.getId(), messageContents);
        fcmService.sendFcmMessage(memberService.findById(peerId), fcmTitle, messageContents);
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "피어 테스트 응답 요청 완료"));

    }

}
