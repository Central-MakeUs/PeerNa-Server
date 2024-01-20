package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.PeerGrade;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.requestDto.TestRequestDto;
import cmc.peerna.web.dto.responseDto.TestResponseDto;

public interface TestService {
    void saveSelfTest(Member member, MemberRequestDto.selfTestDto request);

    void deleteSelfTest(Member member);

    TestResponseDto.selfTestResultResponseDto saveAndGetSelfTestResult(Member member);

    TestResponseDto.selfTestResultResponseDto getSelfTestResult(Member member);

    void deleteSelfTestResult(Member member);

    void savePeerTest(Member writer, Member target, TestRequestDto.peerTestRequestDto request);
    void savePeerFeedBack(Member writer, Member target, String feedback);
    void savePeerGradeResult(Member writer, Member target, PeerGrade peerGrade);
}
