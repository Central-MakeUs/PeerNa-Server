package cmc.peerna.converter;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.PeerFeedback;
import cmc.peerna.domain.enums.SocialType;
import cmc.peerna.service.MemberService;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import cmc.peerna.web.dto.responseDto.RootResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public static MemberResponseDto.MemberMyPageInfoDto toSimpleInfoDto(Member member) {
        return MemberResponseDto.MemberMyPageInfoDto.builder()
                .name(member.getName())
                .testType(member.getSelfTestType())
                .part(member.getPart())
                .job(member.getJob())
                .totalScore(member.getTotalScore())
                .oneLiner(member.getOneliner())
                .build();
    }

    public static RootResponseDto.AllFeedbackDto toFeedbackString(Page<PeerFeedback> feedbackList, Member member) {
        List<String> feedbackContentList = feedbackList.stream()
                .map(feedback -> feedback.getContents())
                .collect(Collectors.toList());

        return RootResponseDto.AllFeedbackDto.builder()
                .feedbackList(feedbackContentList)
                .isFirst(feedbackList.isFirst())
                .isLast(feedbackList.isLast())
                .totalPage(feedbackList.getTotalPages())
                .totalElements(feedbackList.getTotalElements())
                .currentPageElements(feedbackList.getNumberOfElements())
                .build();
    }

    public static MemberResponseDto.memberSimpleProfileDto toMemberSimpleProfileDto(Member member) {

        return MemberResponseDto.memberSimpleProfileDto.builder()
                .memberId(member.getId())
                .name(member.getName())
                .job(member.getJob())
                .part(member.getPart())
                .peerTestType(member.getPeerTestType())
                .oneLiner(member.getOneliner())
                .totalScore(member.getTotalScore())
                .build();
    }

    public static RootResponseDto.memberSimpleDtoPage toSearchByPeerTypeDto(Page<Member> memberPage) {
//        if(memberPage.getTotalElements()==0L) return null;
        List<MemberResponseDto.memberSimpleProfileDto> memberSimpleProfileDtoList = memberPage.stream()
                .map(member -> toMemberSimpleProfileDto(member))
                .collect(Collectors.toList());

        return RootResponseDto.memberSimpleDtoPage.builder()
                .memberSimpleProfileDtoList(memberSimpleProfileDtoList)
                .isFirst(memberPage.isFirst())
                .isLast(memberPage.isLast())
                .totalPage(memberPage.getTotalPages())
                .totalElements(memberPage.getTotalElements())
                .currentPageElements(memberPage.getNumberOfElements())
                .build();
    }

    public static MemberResponseDto.memberBasicInfoDto toMemberBasicInfoDto(Member member) {
        return MemberResponseDto.memberBasicInfoDto.builder()
                .name(member.getName())
                .job(member.getJob())
                .part(member.getPart())
                .uuid(member.getUuid())
                .oneLiner(member.getOneliner())
                .build();
    }

    public static MemberRequestDto.profileUpdateDto toProfileUpdateDto(Member member) {
        return MemberRequestDto.profileUpdateDto.builder()
                .job(member.getJob())
                .part(member.getPart())
                .oneLiner(member.getOneliner())
                .build();
    }

}
