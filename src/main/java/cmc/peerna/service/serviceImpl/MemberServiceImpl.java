package cmc.peerna.service.serviceImpl;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.PeerGradeResult;
import cmc.peerna.domain.enums.SocialType;
import cmc.peerna.jwt.JwtProvider;
import cmc.peerna.repository.MemberRepository;
import cmc.peerna.repository.PeerGradeResultRepository;
import cmc.peerna.service.MemberService;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PeerGradeResultRepository peerGradeResultRepository;

    private final JwtProvider jwtProvider;


    @Override
    public MemberResponseDto.MemberGetTestDto findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ResponseStatus.MEMBER_NOT_FOUND));
        return MemberResponseDto.MemberGetTestDto.builder()
                .id(member.getId())
                .oneLiner(member.getOneliner())
                .name(member.getName())
                .build();

    }

    @Override
    @Transactional
    public Member loginWithKakao(String kakaoId) {
        Optional<Member> oauthMember = memberRepository.findBySocialTypeAndSocialId(SocialType.KAKAO, kakaoId);

        if (!oauthMember.isPresent()) {
            Member savedMember = memberRepository.save(MemberConverter.toMember(kakaoId, SocialType.KAKAO));
            return savedMember;
        } else return oauthMember.get();

//        if (oauthMember.isPresent()) {
//            String token = jwtProvider.createToken(oauthMember.get().getId(), List.of(new SimpleGrantedAuthority("USER")));
//            return LoginResponseDto.builder()
//                    .accessToken(token)
//                    .build();
//        } else{
//            Member savedMember = memberRepository.save(MemberConverter.toMember(kakaoId, SocialType.KAKAO));
//            String token = jwtProvider.createToken(savedMember.getId(), List.of(new SimpleGrantedAuthority("USER")));
//            return LoginResponseDto.builder()
//                    .accessToken(token)
//                    .build();
//
//        }
    }

    @Transactional
    @Override
    public MemberResponseDto.memberBasicInfoDto saveMemberBasicInfo(Member member, MemberRequestDto.basicInfoDTO request) {
        member.setBasicInfo(request);
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
        if(totalScore>100) totalScore=100;
        member.updateTotalScore(totalScore);
    }

    @Override
    public Member findMemberByUuid(String uuid) {
        Member member = memberRepository.findByUuid(uuid).orElseThrow(() -> new MemberException(ResponseStatus.MEMBER_NOT_FOUND));
        return member;
    }

    @Override
    @Transactional
    public MemberRequestDto.profileUpdateDto updateMemberProfile(Member member, MemberRequestDto.profileUpdateDto request) {
        member.updateProfile(request);
        return MemberConverter.toProfileUpdateDto(member);
    }
}
