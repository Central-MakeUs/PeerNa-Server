package cmc.peerna.service.serviceImpl;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.SocialType;
import cmc.peerna.jwt.JwtProvider;
import cmc.peerna.jwt.LoginResponseDto;
import cmc.peerna.repository.MemberRepository;
import cmc.peerna.service.MemberService;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;


    @Override
    public MemberResponseDto.MemberBaseDto findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ResponseStatus.MEMBER_NOT_FOUND));
        return MemberResponseDto.MemberBaseDto.builder()
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
    public void saveMemberBasicInfo(Member member, MemberRequestDto.basicInfoDTO request) {
        member.setBasicInfo(request);

    }

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberException(ResponseStatus.MEMBER_NOT_FOUND));

    }




}
