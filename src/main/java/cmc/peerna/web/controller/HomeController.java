package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.domain.Member;
import cmc.peerna.fcm.service.FcmService;
import cmc.peerna.jwt.handler.annotation.AuthMember;
import cmc.peerna.service.MemberService;
import cmc.peerna.service.RootService;
import cmc.peerna.validation.annotation.CheckPage;
import cmc.peerna.web.dto.responseDto.HomeResponseDto;
import cmc.peerna.web.dto.responseDto.RootResponseDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "2000", description = "OK 성공"),
        @ApiResponse(responseCode = "4008", description = "토큰이 올바르지 않습니다."),
        @ApiResponse(responseCode = "4009", description = "리프레쉬 토큰이 유효하지 않습니다. 다시 로그인 해주세요"),
        @ApiResponse(responseCode = "4010", description = "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
        @ApiResponse(responseCode = "4011", description = "모든 토큰이 만료되었습니다. 다시 로그인해주세요."),
        @ApiResponse(responseCode = "5000", description = "서버 에러, 로빈에게 알려주세요."),
})
@Tag(name = "홈 화면 관련 API 목록", description = "홈 화면 관련 API 목록입니다.")

public class HomeController {


    private final MemberService memberService;
    private final RootService rootService;


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
    @GetMapping("/home/search/peer-type")
    public ResponseDto<RootResponseDto.memberSimpleDtoPage> searchByPeerType(@RequestParam(name = "peerType") String peerType, @CheckPage @RequestParam(name = "page") Integer page, @AuthMember Member member) {
        if (page == null)
            page = 1;
        else if (page < 1)
            throw new MemberException(ResponseStatus.UNDER_PAGE_INDEX_ERROR);
        page -= 1;


        RootResponseDto.memberSimpleDtoPage memberListByPeerType = rootService.getMemberListByPeerType(member, peerType, page);
        return ResponseDto.of(memberListByPeerType);
    }


    @Operation(summary = "파트(역할군)로 동료 찾기 API ✔️🔑", description = "파트(역할군)로 동료 찾기 API입니다.")
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @ApiResponses({
            @ApiResponse(responseCode = "2103", description = "OK, 해당 조건을 만족하는 멤버가 존재하지 않습니다."),
            @ApiResponse(responseCode = "2105",description = "BAD_REQUEST, Part는 PLANNER, DESIGNER, FRONT_END, BACK_END, MARKETER, OTHER 중 하나의 값이어야 합니다."),
            @ApiResponse(responseCode = "4012", description = "BAD_REQUEST, 페이지 번호는 1 이상이여야 합니다."),
            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST, 페이지 번호가 페이징 범위를 초과했습니다.")
    })
    @GetMapping("/home/search/peer-part")
    public ResponseDto<RootResponseDto.memberSimpleDtoPage> searchByPart(@RequestParam(name = "part") String part, @CheckPage @RequestParam(name = "page") Integer page, @AuthMember Member member) {
        if (page == null)
            page = 1;
        else if (page < 1)
            throw new MemberException(ResponseStatus.UNDER_PAGE_INDEX_ERROR);
        page -= 1;

        RootResponseDto.memberSimpleDtoPage memberListByPart = rootService.getMemberListByPart(member, part, page);
        return ResponseDto.of(memberListByPart);
    }


    @Operation(summary = "동료 상세 페이지 조회 API ✔️🔑", description = "동료 상세 페이지 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우.")
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/home/{peer-id}/peer-detail")
    public ResponseDto<HomeResponseDto.peerDetailPageDto> getPeerDetailPage(@PathVariable(name="peer-id") Long peerId, @AuthMember Member member) {
        HomeResponseDto.peerDetailPageDto peerDetailPageDto = rootService.getPeerDetailPageDto(member, memberService.findById(peerId));
        return ResponseDto.of(peerDetailPageDto);
    }

    @Operation(summary = "동료 상세 - 피드백 더보기 API ✔️🔑", description = "동료 상세 - 피드백 더보기 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우."),
            @ApiResponse(responseCode = "4012", description = "BAD_REQUEST , 페이지 번호는 1 이상이여야 합니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST , 페이지 번호가 페이징 범위를 초과했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/home/{peer-id}/peer-feedback")
    public ResponseDto<RootResponseDto.AllFeedbackDto> seeMoreFeedback(@PathVariable(name="peer-id") Long peerId, @CheckPage @RequestParam(name = "page") Integer page, @AuthMember Member member) {
        if (page == null)
            page = 1;
        else if (page < 1)
            throw new MemberException(ResponseStatus.UNDER_PAGE_INDEX_ERROR);
        page -= 1;

        Member peer = memberService.findById(peerId);
        RootResponseDto.AllFeedbackDto feedbackList = rootService.getFeedbackList(peer, page);
        return ResponseDto.of(feedbackList);
    }
}
