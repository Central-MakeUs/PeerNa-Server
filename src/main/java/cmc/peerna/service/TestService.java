package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.PeerCard;
import cmc.peerna.domain.enums.PeerGrade;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.requestDto.TestRequestDto;
import cmc.peerna.web.dto.responseDto.TestResponseDto;

import java.util.List;
import java.util.UUID;

public interface TestService {
    void saveSelfTest(Member member, MemberRequestDto.selfTestDto request);

    void deleteSelfTest(Member member);

    TestResponseDto.selfTestResultResponseDto saveAndGetSelfTestResult(Member member);

    TestResponseDto.selfTestResultResponseDto getSelfTestResult(Member member);

    void deleteSelfTestResult(Member member);

    void savePeerTest(Member writer, Member target, TestRequestDto.peerTestRequestDto request);

    void savePeerFeedBack(Member writer, Member target, String feedback, String uuid);

    void savePeerGradeResult(Member writer, Member target, PeerGrade peerGrade, String uuid);

    void updatePeerTestMemberId(Member member, String uuid);

    List<PeerCard> getPeerTestCard(Member target);

    void checkExistPeerTest(Long writerId, Long targetId);

    boolean checkForSendPeerTestUpdateNotice(Member target);

    boolean checkGuestPeerTestIpAddress(String uuid, String ipAddress);
    void savePeerTestIpAddress(String uuid, String ipAddress);
}
