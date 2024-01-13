package cmc.peerna.web.controller;

import cmc.peerna.apiResponse.response.ResponseDto;
import cmc.peerna.service.MemberService;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    @GetMapping("/member/{memberId}")
    public ResponseDto<MemberResponseDto.MemberBaseDto> searchMember(@PathVariable(name="memberId") Long memberId){
        MemberResponseDto.MemberBaseDto memberDto = memberService.findMember(memberId);
        return ResponseDto.of(memberDto);
    }
}
