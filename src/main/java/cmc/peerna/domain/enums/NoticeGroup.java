package cmc.peerna.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeGroup {
    PROJECT("프로젝트 관련 알림"),
    PEER_TEST("피어테스트 관련 알림");

    private final String description;
}
