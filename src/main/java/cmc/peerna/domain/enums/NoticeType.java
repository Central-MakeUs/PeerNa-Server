package cmc.peerna.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeType {

    // Project 관련
    INVITE_TO_PROJECT("프로젝트 초대"),
    REQUEST_JOIN_PROJECT("프로젝트 참여 신청"),
    ACCEPT_PROJECT_INVITATION("프로젝트 초대 수락"),
    DECLINE_PROJECT_INVITATION("프로젝트 초대 거절"),
    ACCEPT_PROJECT_JOIN_REQUEST("프로젝트 참여 요청 수락"),
    DECLINE_PROJECT_JOIN_REQUEST("프로젝트 참여 요청 거절"),

    // Peer-Test 관련
    PEER_TEST_REQUEST("피어테스트 요청"),
    PEER_TEST_RESULT_UPDATE("피어테스트 분석 결과 업데이트");

    private final String description;
}
