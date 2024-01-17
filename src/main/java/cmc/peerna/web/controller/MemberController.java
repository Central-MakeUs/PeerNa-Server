package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.UserRole;
import cmc.peerna.feign.dto.KakaoTokenInfoResponseDto;
import cmc.peerna.feign.service.AccountService;
import cmc.peerna.jwt.JwtProvider;
import cmc.peerna.jwt.LoginResponseDto;
import cmc.peerna.redis.service.RedisService;
import cmc.peerna.service.MemberService;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


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

        response.sendRedirect(webRedirectUrl);

        return ResponseDto.of(LoginResponseDto.builder()
                .memberId(member.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());

    }

}

