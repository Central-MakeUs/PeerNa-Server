package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.feign.dto.KakaoTokenInfoResponseDto;
import cmc.peerna.feign.service.AccountService;
import cmc.peerna.jwt.SignResponseDto;
import cmc.peerna.service.MemberService;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final AccountService accountService;
    private final MemberService memberService;


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
    public ResponseDto<SignResponseDto> kakaoLogin(@RequestParam(value = "code") String code)  {
        log.info("리다이렉트 완료");
        log.info("코드 : " + code);
        KakaoTokenInfoResponseDto kakaoUserInfo = accountService.getKakaoUserInfo(code);
        log.info("유저 id" + kakaoUserInfo.getId());
        return ResponseDto.of(memberService.loginWithKakao(kakaoUserInfo.getId()));
    }
}

