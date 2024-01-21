package cmc.peerna.converter;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.SocialType;
import cmc.peerna.service.MemberService;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class MemberConverter {
    private static MemberService memberService;
    public static Member toMember(String socialId, SocialType socialType) {
        return Member.builder()
                .socialId((socialId))
                .socialType(socialType)
                .build();
    }

    public static Member toMemberById(Long memberId) {
        return memberService.findById(memberId);
    }

    public static MemberResponseDto.MemberStatusDto toMemberStatusDto(Long memberId, String status) {
        return MemberResponseDto.MemberStatusDto.builder()
                .memberId(memberId)
                .status(status)
                .calledAt(LocalDateTime.now())
                .build();
    }

    public static MemberResponseDto.MemberSimpleInfoDto toSimpleInfoDto(Member member) {
        return MemberResponseDto.MemberSimpleInfoDto.builder()
                .name(member.getName())
                .testType(member.getTestType())
                .part(member.getPart())
                .job(member.getJob())
                .positiveness(member.getPositiveness())
                .oneLiner(member.getOneliner())
                .build();
    }
}
