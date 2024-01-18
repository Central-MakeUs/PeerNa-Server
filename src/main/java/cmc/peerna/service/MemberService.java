package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.jwt.LoginResponseDto;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;

public interface MemberService {

    MemberResponseDto.MemberBaseDto findMember(Long memberId);

    Member loginWithKakao(String kakaoId);
    void saveMemberBasicInfo(Member member, MemberRequestDto.basicInfoDTO request);

    Member findById(Long memberId);
}
