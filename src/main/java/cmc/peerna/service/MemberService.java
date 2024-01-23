package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.redis.domain.RefreshToken;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;

public interface MemberService {

    MemberResponseDto.MemberGetTestDto findMember(Long memberId);

    Member loginWithKakao(String kakaoId);
    MemberResponseDto.memberBasicInfoDto saveMemberBasicInfo(Member member, MemberRequestDto.basicInfoDTO request);

    Member findById(Long memberId);

    void updateTotalScore(Member member);

    Member findMemberByUuid(String uuid);
    MemberRequestDto.profileUpdateDto updateMemberProfile(Member member, MemberRequestDto.profileUpdateDto request);

    String regenerateAccessToken(RefreshToken refreshToken);
}
