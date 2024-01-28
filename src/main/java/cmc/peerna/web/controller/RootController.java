package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.fcm.service.FcmService;
import cmc.peerna.jwt.handler.annotation.AuthMember;
import cmc.peerna.service.MemberService;
import cmc.peerna.service.RootService;
import cmc.peerna.validation.annotation.CheckPage;
import cmc.peerna.web.dto.requestDto.RootRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import cmc.peerna.web.dto.responseDto.RootResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "2000",description = "OK 성공"),
        @ApiResponse(responseCode = "5000",description = "서버 에러, 로빈에게 알려주세요."),
})
@Tag(name = "테스트, 기타 API", description = "테스트, 기타 API 목록입니다.")
public class RootController {

    private final MemberService memberService;
    private final FcmService fcmService;
    private final RootService rootService;

    @GetMapping("/health")
    public String healthCheck() {
        return "I'm healthy!";
    }

    @Operation(summary = "POST 요청 TEST API ✔️", description = "POST 요청 테스트용 API입니다. <br> body에 \"peerna\" 를 넣은 경우 4101 반환")
    @ApiResponses({
            @ApiResponse(responseCode = "4101",description = "BAD_REQUEST, body에 \"peerna\" 를 넣은 경우")
    })
    @PostMapping("/test/ping")
    public ResponseDto<String> postTest(@RequestBody RootRequestDto.PostTestDto body) {
        if (body.getBody().equals("peerna")) {
            throw new MemberException(ResponseStatus.WRONG_POST_TEST);
        }
        return ResponseDto.of(body.getBody());
    }

    // TEST API
    @GetMapping("/test/{member-id}")
    @Operation(summary = "GET 요청 TEST API ✔️", description = "GET 요청 테스트용 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200",description = "BAD_REQUEST, 존재하지 않는 유저입니다.")
    })
    public ResponseDto<MemberResponseDto.MemberGetTestDto> searchMember(@PathVariable(name = "member-id") Long memberId) {
        MemberResponseDto.MemberGetTestDto memberDto = memberService.findMember(memberId);
        return ResponseDto.of(memberDto);
    }

    @Operation(summary = "FCM 테스트 API", description = "테스트용")
    @PostMapping("/test/fcm")
    public ResponseDto<Object> testFCM(@RequestBody RootRequestDto.FCMTestDto fcmToken) throws IOException
    {
        fcmService.testFCMService(fcmToken.getFcmToken());
        return ResponseDto.of("FCM 테스트 성공!");
    }


    @Operation(summary = "피어 유형으로 동료 찾기 API ✔️🔑", description = "피어 유형으로 동료 찾기 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2103",description = "OK, 해당 조건을 만족하는 멤버가 존재하지 않습니다."),
            @ApiResponse(responseCode = "2104",description = "BAD_REQUEST, TestType은 D,I,S,C 중 하나의 값이어야 합니다."),
            @ApiResponse(responseCode = "4012",description = "BAD_REQUEST, 페이지 번호는 1 이상이여야 합니다."),
            @ApiResponse(responseCode = "4013",description = "BAD_REQUEST, 페이지 번호가 페이징 범위를 초과했습니다.")
    })
    @GetMapping("/home/peer-type")
    public ResponseDto<RootResponseDto.SearchByPeerTypeDto> searchByPeerType(@RequestParam(name = "peerType") String request, @CheckPage @RequestParam(name = "page") Integer page, @AuthMember Member member) {
        if (page == null)
            page = 1;
        else if (page < 1)
            throw new MemberException(ResponseStatus.UNDER_PAGE_INDEX_ERROR);
        page -= 1;


        RootResponseDto.SearchByPeerTypeDto memberListByPeerType = rootService.getMemberListByPeerType(member, request, page);
        return ResponseDto.of(memberListByPeerType);

    }
}
