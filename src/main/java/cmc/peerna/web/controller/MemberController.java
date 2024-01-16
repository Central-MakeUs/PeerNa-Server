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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final AccountService accountService;
    private final MemberService memberService;
    private final RedisService redisService;
    private final JwtProvider jwtProvider;


    // TEST API
    @GetMapping("/member/{memberId}")
    public ResponseDto<MemberResponseDto.MemberBaseDto> searchMember(@PathVariable(name = "memberId") Long memberId) {
        MemberResponseDto.MemberBaseDto memberDto = memberService.findMember(memberId);
        return ResponseDto.of(memberDto);
    }

    // 카카오 로그인 정보 입력 테스트용
    @GetMapping("/login/kakao")
    public ResponseEntity<Object> kakaoCode()  {

        HttpHeaders httpHeaders = accountService.kakaoLoginRequestHeader();
        return httpHeaders != null ?
                new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER):
                ResponseEntity.badRequest().build();
    }

    //리다이렉트 주소
    @GetMapping("/login/oauth2/kakao")
    public ResponseDto<LoginResponseDto> kakaoLogin(@RequestParam(value = "code") String code)  {
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

        return ResponseDto.of(LoginResponseDto.builder()
                .memberId(member.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());

    }

}

