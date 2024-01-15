package cmc.peerna.service.serviceImpl;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberExceptionHandler;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.SocialType;
import cmc.peerna.feign.KakaoInfoFeignClient;
import cmc.peerna.feign.dto.KakaoMemberInfo;
import cmc.peerna.jwt.JwtProvider;
import cmc.peerna.jwt.SignResponseDto;
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


    @Override
    public MemberResponseDto.MemberBaseDto findMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberExceptionHandler(ResponseStatus.NOT_EXIST_MEMBER));
        return MemberResponseDto.MemberBaseDto.builder()
                .id(member.getId())
                .oneLiner(member.getOneliner())
                .name(member.getName())
                .build();

    }




}
