package cmc.peerna.converter;

import cmc.peerna.domain.Notice;
import cmc.peerna.web.dto.responseDto.NoticeResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class NoticeConverter {

    public static NoticeResponseDto.NoticeSimpleInfoDto toNoticeSimpleProfile(Notice notice) {
        return NoticeResponseDto.NoticeSimpleInfoDto.builder()
                .targetId(notice.getTargetId())
                .noticeType(notice.getNoticeType())
                .contents(notice.getContents())
                .createdTime(notice.getCreatedAt())
                .readFlag(notice.getReadFlag())
                .build();
    }

    public static NoticeResponseDto.NoticePageDto toNoticePageDto(Page<Notice> noticePage) {
        if(noticePage.getTotalElements()==0L) return null;
        List<NoticeResponseDto.NoticeSimpleInfoDto> noticeSimpleInfoDtoList = noticePage.stream()
                .map(notice -> toNoticeSimpleProfile(notice))
                .collect(Collectors.toList());

        return NoticeResponseDto.NoticePageDto.builder()
                .noticeList(noticeSimpleInfoDtoList)
                .isFirst(noticePage.isFirst())
                .isLast(noticePage.isLast())
                .totalPage(noticePage.getTotalPages())
                .totalElements(noticePage.getTotalElements())
                .currentPageElements(noticePage.getNumberOfElements())
                .build();
    }
}
