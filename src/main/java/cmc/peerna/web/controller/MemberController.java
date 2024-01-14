package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.jwt.SignResponseDto;
import cmc.peerna.service.MemberService;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/{memberId}")
    public ResponseDto<MemberResponseDto.MemberBaseDto> searchMember(@PathVariable(name = "memberId") Long memberId) {
        MemberResponseDto.MemberBaseDto memberDto = memberService.findMember(memberId);
        return ResponseDto.of(memberDto);
    }

    @PostMapping("/member/oauth")
    public ResponseDto<SignResponseDto> oauth(@RequestBody MemberRequestDto.OAuthDTO request) {
        System.out.println("토큰 : " + request.getAccessToken());
        SignResponseDto token = memberService.loginWithKakao(request);
        return ResponseDto.of(token);
    }
}

