package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.SocialType;
import cmc.peerna.redis.domain.RefreshToken;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;

public interface MemberService {

    MemberResponseDto.MemberGetTestDto findMember(Long memberId);

    Member socialLogin(String socialId, SocialType socialType);
    MemberResponseDto.memberBasicInfoDto saveMemberBasicInfo(Member member, MemberRequestDto.basicInfoDTO request);

    MemberResponseDto.memberBasicInfoDto getMemberBasicInfo(Member member);

    Member findById(Long memberId);

    void updateTotalScore(Member member);

    Member findMemberByUuid(String uuid);
    MemberRequestDto.profileUpdateDto updateMemberProfile(Member member, MemberRequestDto.profileUpdateDto request);

    String regenerateAccessToken(RefreshToken refreshToken);

    boolean agreePush(Member member, MemberRequestDto.pushAgreeDto request);

    void withdrawal(Member member);
}
