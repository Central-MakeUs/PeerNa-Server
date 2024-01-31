package cmc.peerna.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeType {
    INVITED_TO_OTHER_PROJECT("남의 프로젝트에 참여 제안 받음"),
    OTHER_USER_REQUESTED_MY_PROJECT("남이 내 프로젝트에 참여 신청함."),
    OTHER_PROJECT_MY_REQUEST_ACCEPTED("남의 프로젝트에 대한 내 참여 요청이 수락됨."),
    OTHER_PROJECT_MY_REQUEST_DECLINED("남의 프로젝트에 대한 내 참여 요청이 거절됨."),
    MY_PROJECT_MY_INVITE_ACCEPTED("나의 프로젝트에 대한 내 참여 제안이 수락됨."),
    MY_PROJECT_MY_INVITE_DECLINED("나의 프로젝트에 대한 내 참여 제안이 거절됨.");

    private final String description;
}
