package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.UserRole;
import cmc.peerna.feign.dto.KakaoTokenInfoResponseDto;
import cmc.peerna.feign.service.AccountService;
import cmc.peerna.jwt.JwtProvider;
import cmc.peerna.jwt.LoginResponseDto;
import cmc.peerna.jwt.handler.annotation.AuthMember;
import cmc.peerna.redis.service.RedisService;
import cmc.peerna.service.MemberService;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final AccountService accountService;
    private final MemberService memberService;
    private final RedisService redisService;
    private final JwtProvider jwtProvider;

    @Value("${web.redirect-url}")
    String webRedirectUrl;


    // 카카오 로그인 정보 입력 테스트용
    @Operation(summary = "XX 백엔드 테스트용, 사용XX")
    @GetMapping("/login/kakao")
    public ResponseEntity<Object> kakaoCode()  {

        HttpHeaders httpHeaders = accountService.kakaoLoginRequestHeader();
        return httpHeaders != null ?
                new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER):
                ResponseEntity.badRequest().build();
    }

    //리다이렉트 주소
    @Operation(summary = "XX 카카오 소셜 로그인 Redirect 주소, 사용XX")
    @GetMapping("/login/oauth2/kakao")
    public ResponseDto<LoginResponseDto> kakaoLogin(@RequestParam(value = "code") String code, HttpServletResponse response) throws IOException {
        KakaoTokenInfoResponseDto kakaoUserInfo = accountService.getKakaoUserInfo(code);
        Member member = memberService.loginWithKakao(kakaoUserInfo.getId());
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
        redirectUrl += "?"+
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

    @Operation(summary = "유저 기본 정보 저장 API ✔️", description = "유저의 기본 정보를 저장하는 API입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "2000",description = "OK 성공"),
            @ApiResponse(responseCode = "2200",description = "BAD_REQUEST, 존재하지 않는 유저를 조회한 경우.")
    })
    @Parameters({
            @Parameter(name = "member", hidden = true)
    })
    @PostMapping("member/basic-info")
    public ResponseDto<MemberResponseDto.MemberStatusDto> saveMemberInfo(@AuthMember Member member, @RequestBody MemberRequestDto.basicInfoDTO request) {
        memberService.saveMemberBasicInfo(member, request);
        return ResponseDto.of(MemberConverter.toMemberStatusDto(member.getId(), "SaveMemberBasicInfo"));
    }



}

