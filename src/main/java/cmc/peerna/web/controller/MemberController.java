package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.feign.dto.KakaoTokenInfoResponseDto;
import cmc.peerna.feign.service.AccountService;
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

    @GetMapping("/login/kakao")
    public ResponseEntity<Object> kakaoCode()  {
        HttpHeaders httpHeaders = accountService.kakaoLoginRequestHeader();
        return httpHeaders != null ?
                new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER):
                ResponseEntity.badRequest().build();
    }

    //리다이렉트 주소
    @GetMapping("/login/oauth2/kakao")
    public ResponseEntity<Object> kakaoLogin(@RequestParam(value = "code") String code)  {
        KakaoTokenInfoResponseDto kakaoUserInfo = accountService.getKakaoUserInfo(code);
        log.info("유저 id" + kakaoUserInfo.getId());
        return null;
    }
}

