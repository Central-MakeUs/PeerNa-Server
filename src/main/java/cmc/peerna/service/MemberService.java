package cmc.peerna.service;

import cmc.peerna.web.dto.responseDto.MemberResponseDto;

public interface MemberService {
    MemberResponseDto.MemberBaseDto findMember(Long memberId);
}
