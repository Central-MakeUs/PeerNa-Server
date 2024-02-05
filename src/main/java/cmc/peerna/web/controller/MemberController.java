package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.apiResponse.response.PageResponseDto;
import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.SocialType;
import cmc.peerna.domain.enums.UserRole;
import cmc.peerna.feign.dto.KakaoTokenInfoResponseDto;
import cmc.peerna.feign.service.AccountService;
import cmc.peerna.jwt.JwtProvider;
import cmc.peerna.jwt.LoginResponseDto;
import cmc.peerna.jwt.handler.annotation.AuthMember;
import cmc.peerna.redis.domain.RefreshToken;
import cmc.peerna.redis.service.RedisService;
import cmc.peerna.service.AppleService;
import cmc.peerna.service.MemberService;
import cmc.peerna.service.RootService;
import cmc.peerna.validation.annotation.CheckPage;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.requestDto.RootRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import cmc.peerna.web.dto.responseDto.RootResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;


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
@Tag(name = "Member 관련 API 목록", description = "Member 관련 API 목록입니다.")

public class MemberController {

    private final AccountService accountService;
    private final MemberService memberService;
    private final RedisService redisService;
    private final RootService rootService;
    private final JwtProvider jwtProvider;
    private final AppleService appleService;

    @Value("${web.redirect-url}")
    String webRedirectUrl;

    @Value("${web.apple-redirect-url}")
    String appleRedirectUrl;

    // 카카오 로그인 정보 입력 테스트용
    @Operation(summary = "XX 백엔드 테스트용, 사용XX")
    @GetMapping("/login/kakao")
    public ResponseEntity<Object> kakaoCode() {

        HttpHeaders httpHeaders = accountService.kakaoLoginRequestHeader();
        return httpHeaders != null ?
                new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER) :
                ResponseEntity.badRequest().build();
    }

    //리다이렉트 주소
    @Operation(summary = "XX 카카오 소셜 로그인 Redirect 주소, 사용XX")
    @GetMapping("/login/oauth2/kakao")
    public ResponseDto<LoginResponseDto> kakaoLogin(@RequestParam(value = "code") String code, HttpServletResponse response) throws IOException {
        KakaoTokenInfoResponseDto kakaoUserInfo = accountService.getKakaoUserInfo(code);
        Member member = memberService.socialLogin(kakaoUserInfo.getId(), SocialType.KAKAO);
        String accessToken =
                jwtProvider.createAccessToken(
                        member.getId(),
                        member.getSocialType().toString(),
                        member.getSocialId(),
                        List.of(new SimpleGrantedAuthority(UserRole.USER.name())));

        String refreshToken =
                redisService
                        .generateRefreshToken(member.getSocialId(), member.getSocialType())
                        .getToken();

        String redirectUrl = webRedirectUrl;
        redirectUrl += "?" +
                "memberId=" + member.getId()
                + "&accessToken=" + accessToken
                + "&refreshToken=" + refreshToken;

        response.sendRedirect(redirectUrl);

        return ResponseDto.of(LoginResponseDto.builder()
                .memberId(member.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }

    @Operation(summary = "XX 애플 소셜 로그인 Redirect 주소, 사용XX")
    @ApiResponses({
            @ApiResponse(responseCode = "4014", description = "Identity Token에서 유효한 값을 찾지 못했습니다."),
            @ApiResponse(responseCode = "4015", description = "BAD_REQUEST, Identity Token의 형태가 잘못되었습니다.")
    })
    @PostMapping(value = "/member/login/oauth2/apple")
    public ResponseDto<LoginResponseDto> appleLogin(@RequestParam(value = "code") String code, @RequestParam(value = "id_token") String id_token, HttpServletResponse response) throws IOException {
        String socialId = appleService.userIdFromApple(id_token);
        log.info("Apple 인증 서버로부터 받은 userId : " + socialId);
        Member member = memberService.socialLogin(socialId, SocialType.APPLE);

        String accessToken =
                jwtProvider.createAccessToken(
                        member.getId(),
                        member.getSocialType().toString(),
                        member.getSocialId(),
                        List.of(new SimpleGrantedAuthority(UserRole.USER.name())));

        String refreshToken =
                redisService
                        .generateRefreshToken(member.getSocialId(), member.getSocialType())
                        .getToken();

        String redirectUrl = appleRedirectUrl;
        redirectUrl += "?" +
                "memberId=" + member.getId()
                + "&accessToken=" + accessToken
                + "&refreshToken=" + refreshToken;

        response.sendRedirect(redirectUrl);

        return ResponseDto.of(LoginResponseDto.builder()
                .memberId(member.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }


    @Operation(summary = "유저 기본 정보 저장 API ✔️🔑", description = "유저의 기본 정보를 저장하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우.")
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PostMapping("/member/basic-info")
    public ResponseDto<MemberResponseDto.memberBasicInfoDto> saveMemberInfo(@AuthMember Member member, @RequestBody MemberRequestDto.basicInfoDTO request) {
        return ResponseDto.of(memberService.saveMemberBasicInfo(member, request));
    }

    @Operation(summary = "마이페이지 조회 API ✔️🔑", description = "마이페이지 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우.")
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/member/mypage")
    public ResponseDto<RootResponseDto.MypageDto> getMyPage(@AuthMember Member member) {
        RootResponseDto.MypageDto myPageDto = rootService.getMyPageDto(member);
        return ResponseDto.of(myPageDto);
    }

    @Operation(summary = "피드백 더보기 API ✔️🔑", description = "피드백 더보기 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우."),
            @ApiResponse(responseCode = "4012", description = "BAD_REQUEST , 페이지 번호는 1 이상이여야 합니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
            @ApiResponse(responseCode = "4013", description = "BAD_REQUEST , 페이지 번호가 페이징 범위를 초과했습니다.", content = @Content(schema = @Schema(implementation = ResponseDto.class))),
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/member/mypage/feedback")
    public PageResponseDto<List<String>> seeMoreFeedback(@CheckPage @RequestParam(name = "page") Integer page, @AuthMember Member member) {
        if (page == null)
            page = 1;
        else if (page < 1)
            throw new MemberException(ResponseStatus.UNDER_PAGE_INDEX_ERROR);
        page -= 1;

        RootResponseDto.AllFeedbackDto feedbackList = rootService.getFeedbackList(member, page);

        List<String> feedBackStringList = feedbackList.getFeedbackList();
        RootRequestDto.PageRequestDto pageRequestDto = RootRequestDto.PageRequestDto.builder()
                .totalElements(feedbackList.getTotalElements())
                .currentPageElements(feedbackList.getCurrentPageElements())
                .totalPage(feedbackList.getTotalPage())
                .isFirst(feedbackList.getIsFirst())
                .isLast(feedbackList.getIsLast())
                .build();

        return PageResponseDto.of(feedBackStringList, pageRequestDto);
    }


    @Operation(summary = "유저 프로필 편집 API ✔️🔑", description = "유저 프로필 편집 API입니다.")
    @ApiResponses({
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PatchMapping("/member/mypage/profile")
    public ResponseDto<MemberRequestDto.profileUpdateDto> updateMemberProfile(@AuthMember Member member, @RequestBody MemberRequestDto.profileUpdateDto request) {
        return ResponseDto.of(memberService.updateMemberProfile(member, request));
    }

    @Operation(summary = "UUID로 유저 이름 조회 API ✔️", description = "UUID로 유저 이름 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2250", description = "BAD_REQUEST, 잘못된 UUID 값입니다."),
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/member/name")
    public ResponseDto<MemberResponseDto.memberNameResponseDto> getUserNameByUuid(@RequestParam(name = "uuid") String uuid) {
        Member memberByUuid = memberService.findMemberByUuid(uuid);
        return ResponseDto.of(MemberResponseDto.memberNameResponseDto.builder().name(memberByUuid.getName()).build());
    }

    @Operation(summary = "refresh token 통해 access token 재발급 API ✔️", description = "refresh token 통해 access token 재발급 API입니다.")
    @PostMapping("/member/new-token")
    public ResponseDto<MemberResponseDto.newTokenDto> getNewAccessToken(@RequestBody MemberRequestDto.ReissueDTO request) {
        RefreshToken newRefreshToken = redisService.reGenerateRefreshToken(request);
        String accessToken = memberService.regenerateAccessToken(newRefreshToken);

        return ResponseDto.of(MemberResponseDto.newTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .build());
    }


    @Operation(summary = "유저 기본 정보 조회 API ✔️🔑", description = "UUID 포함 유저 기본 정보 조회 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우.")
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @GetMapping("/member/me")
    ResponseDto<MemberResponseDto.memberBasicInfoDto> getMemberBasicInfo(@AuthMember Member member) {
        MemberResponseDto.memberBasicInfoDto memberBasicInfo = memberService.getMemberBasicInfo(member);
        return ResponseDto.of(memberBasicInfo);
    }

    @Operation(summary = "푸시 알림 동의 API ✔️🔑", description = "푸시 알림 동의 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우.")
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PostMapping("/member/push-agree")
    ResponseDto<MemberResponseDto.MemberStatusDto> pushAgree(@AuthMember Member member, @RequestBody MemberRequestDto.pushAgreeDto request) {
        boolean pushAgree = memberService.agreePush(member, request);
        String responseMessage = pushAgree==true ? "푸시 알림 허용" : "푸시 알림 거부";
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), responseMessage));
    }


    @Operation(summary = "회원탈퇴 API ✔️🔑", description = "회원탈퇴 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우.")
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PostMapping("/member/withdrawal")
    ResponseDto<MemberResponseDto.MemberStatusDto> withdrawal(@AuthMember Member member) {
        memberService.withdrawal(member);
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "회원탈퇴 완료"));
    }


    @Operation(summary = "로그아웃 API ✔️🔑", description = "로그아웃 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2200", description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우.")
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PostMapping("/member/logout")
    ResponseDto<MemberResponseDto.MemberStatusDto> logout(@AuthMember Member member, @RequestBody MemberRequestDto.logoutRequestDto request) {

        memberService.logout(member, request.getFcmToken());
        redisService.deleteRefreshToken(request.getRefreshToken());

        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "로그아웃 완료"));
    }

}

