package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.responseDto.TestResponseDto;

public interface TestService {
    void saveSelfTest(Member member, MemberRequestDto.selfTestDto request);

    void deleteSelfTest(Member member);

    TestResponseDto.selfTestResultResponseDto saveAndGetSelfTestResult(Member member);
}
