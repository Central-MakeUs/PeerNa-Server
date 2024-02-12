package cmc.peerna.service.serviceImpl;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.*;
import cmc.peerna.domain.enums.SocialType;
import cmc.peerna.domain.enums.TestType;
import cmc.peerna.jwt.JwtProvider;
import cmc.peerna.redis.domain.RefreshToken;
import cmc.peerna.repository.*;
import cmc.peerna.service.MemberService;
import cmc.peerna.utils.TestResultCalculator;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final SelfTestRepository selfTestRepository;
    private final SelfTestResultRepository selfTestResultRepository;
    private final PeerGradeResultRepository peerGradeResultRepository;
    private final PeerFeedbackRepository peerFeedbackRepository;
    private final PeerTestRepository peerTestRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final FcmTokenRepository fcmTokenRepository;

    private final JwtProvider jwtProvider;
    private final TestResultCalculator testResultCalculator;



    @Override
    public MemberResponseDto.MemberGetTestDto findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ResponseStatus.MEMBER_NOT_FOUND));
        return MemberResponseDto.MemberGetTestDto.builder()
                .id(member.getId())
                .oneLiner(member.getOneliner())
                .name(member.getName())
                .build();

    }

    @Transactional
    public Member socialLogin(String socialId, SocialType socialType) {
        Optional<Member> oauthMember = memberRepository.findBySocialTypeAndSocialId(socialType, socialId);

        if (!oauthMember.isPresent()) {
            Member savedMember = memberRepository.save(MemberConverter.toMember(socialId, socialType));
            return savedMember;
        } else return oauthMember.get();

    }

    @Transactional
    @Override
    public MemberResponseDto.memberBasicInfoDto saveMemberBasicInfo(Member member, MemberRequestDto.basicInfoDTO request) {
        member.setBasicInfo(request);
        return MemberConverter.toMemberBasicInfoDto(member);

    }

    @Override
    public MemberResponseDto.memberBasicInfoDto getMemberBasicInfo(Member member) {
        return MemberConverter.toMemberBasicInfoDto(member);
    }


    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ResponseStatus.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updateTotalScore(Member member) {
        Integer totalScore = 0;
        List<PeerGradeResult> peerGradeResultList = peerGradeResultRepository.findAllByTarget(member);
        if (peerGradeResultList.size() < 3) {
            return;
        }
        int scoreSum=0;
        for (PeerGradeResult peerGradeResult : peerGradeResultList) {
            scoreSum += peerGradeResult.getPeerGrade().getScore();
        }
        totalScore = scoreSum / peerGradeResultList.size();
        totalScore = totalScore + projectMemberRepository.countByMember(member);
        if(totalScore>100) totalScore=100;
        member.updateTotalScore(totalScore);
    }

    @Override
    @Transactional
    public void updatePeerTestType(Member member) {
        if(peerGradeResultRepository.countByTarget(member)<3) return;
        List<PeerTest> peerTestList = peerTestRepository.findALlByTarget(member);
        TestType peerTestType = testResultCalculator.peerTestPeerType(peerTestList);
        member.updatePeerTestType(peerTestType);
    }

    @Override
    public Member findMemberByUuid(String uuid) {
        Member member = memberRepository.findByUuid(uuid).orElseThrow(() -> new MemberException(ResponseStatus.UUID_NOT_FOUND));
        return member;
    }

    @Override
    @Transactional
    public MemberRequestDto.profileUpdateDto updateMemberProfile(Member member, MemberRequestDto.profileUpdateDto request) {
        member.updateProfile(request);
        return MemberConverter.toProfileUpdateDto(member);
    }

    @Override
    public String regenerateAccessToken(RefreshToken refreshToken) {
        Member member = memberRepository.findById(refreshToken.getMemberId()).orElseThrow(() -> new MemberException(ResponseStatus.MEMBER_NOT_FOUND));
        log.info("Member Id값 : " + member.getId());
        String accessToken = jwtProvider.createAccessToken(member.getId(), member.getSocialType().toString(), member.getSocialId(), Arrays.asList(new SimpleGrantedAuthority("USER")));
        return accessToken;
    }

    @Override
    @Transactional
    public boolean agreePush(Member member, MemberRequestDto.pushAgreeDto request) {

        return member.updatePushAgree(request.isPushAgree());
    }

    @Override
    @Transactional
    public void saveFcmToken(Member member, MemberRequestDto.saveFcmTokenDto request) {
        if (fcmTokenRepository.existsByMemberAndToken(member, request.getFcmToken())) {
            return;
        }
        fcmTokenRepository.save(FcmToken.builder()
                .member(member)
                .token(request.getFcmToken())
                .build()
        );
    }


    @Override
    @Transactional
    public void logout(Member member, String fcmToken) {
        fcmTokenRepository.deleteByMemberAndToken(member, fcmToken);
    }

    @Override
    @Transactional
    public void withdrawal(Member member) {
        selfTestRepository.deleteAllByWriter(member);
        selfTestResultRepository.deleteByMember(member);
        peerTestRepository.deleteAllByTarget(member);
        peerFeedbackRepository.deleteAllByTarget(member);
        peerGradeResultRepository.deleteAllByTarget(member);

        List<PeerTest> peerTestListByWriter = peerTestRepository.findALlByWriter(member);
        for (PeerTest peerTest : peerTestListByWriter) {
            peerTest.updateWriterToNull();
        }

        List<PeerFeedback> peerFeedbackListByWriter = peerFeedbackRepository.findAllByWriter(member);
        for (PeerFeedback peerFeedback : peerFeedbackListByWriter) {
            peerFeedback.updateWriterToNull();
        }

        List<PeerGradeResult> peerGradeResultListByWriter = peerGradeResultRepository.findAllByWriter(member);
        for (PeerGradeResult peerGradeResult : peerGradeResultListByWriter) {
            peerGradeResult.updateWriterToNull();
        }
        projectMemberRepository.deleteAllByMember(member);
        fcmTokenRepository.deleteAllByMember(member);

        member.withdrawalMember();
    }

}
