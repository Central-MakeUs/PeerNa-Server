package cmc.peerna.service;

import cmc.peerna.jwt.SignResponseDto;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;

public interface MemberService {

    SignResponseDto loginWithKakao(MemberRequestDto.OAuthDTO request);
    MemberResponseDto.MemberBaseDto findMember(Long memberId);
}
