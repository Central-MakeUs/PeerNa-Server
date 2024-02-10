package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.NoticeGroup;
import cmc.peerna.domain.enums.NoticeType;
import cmc.peerna.web.dto.responseDto.NoticeResponseDto;

public interface NoticeService {
    void createNotice(Member sender, Long receiverId, NoticeGroup noticeGroup, NoticeType noticeType, Long targetId, String contents);
    void createProjectRequestNotice(Member sender, Long receiverId, NoticeGroup noticeGroup, NoticeType noticeType, Long targetId, Long subTargetId, String contents);

    NoticeResponseDto.NoticePageDto getNoticePageByNoticeGroup(Member receiver, NoticeGroup noticeGroup, Integer page);

    NoticeResponseDto.NoticePageDto getProjectNoticePage(Member receiver, Integer page);

    NoticeResponseDto.NoticePageDto getPeerTestNoticePage(Member receiver, Integer page);

    boolean existsNotice(Long receiverId, Long senderId, NoticeType noticeType);

    void existsProjectJoinRequestNotice(Long receiverId, Long senderId);

    void existsPeerTestRequestNotice(Long receiverId, Long senderId);
}
