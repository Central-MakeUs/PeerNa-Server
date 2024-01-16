package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.jwt.LoginResponseDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;

public interface MemberService {

    MemberResponseDto.MemberBaseDto findMember(Long memberId);

    Member loginWithKakao(String kakaoId);
}
