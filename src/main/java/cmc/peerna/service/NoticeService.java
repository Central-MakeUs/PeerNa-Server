package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.NoticeGroup;
import cmc.peerna.domain.enums.NoticeType;
import cmc.peerna.web.dto.responseDto.NoticeResponseDto;

public interface NoticeService {
    void createNotice(Member sender, Long receiverId, NoticeType noticeType);

    NoticeResponseDto.NoticePageDto getNoticePageByNoticeGroup(Member receiver, NoticeGroup noticeGroup, Integer page);

    NoticeResponseDto.NoticePageDto getProjectNoticePage(Member receiver, Integer page);
    NoticeResponseDto.NoticePageDto getPeerTestNoticePage(Member receiver, Integer page);
}
