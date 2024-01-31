package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.NoticeType;

public interface NoticeService {
    void createNotice(Member sender, Long receiverId, NoticeType noticeType);
}
