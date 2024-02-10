package cmc.peerna.web.dto.responseDto;

import cmc.peerna.domain.enums.NoticeType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class NoticeResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NoticeSimpleInfoDto {
        private Long targetId;
        private Long subTargetId;

        // 알림의 좌측 아이콘 구분 위한 필드
        private NoticeType noticeType;
        private String contents;
        private LocalDateTime createdTime;

        private String readFlag;

    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class NoticePageDto{
        List<NoticeResponseDto.NoticeSimpleInfoDto> noticeList;
        Long totalElements;
        Integer currentPageElements;
        Integer totalPage;
        Boolean isFirst;
        Boolean isLast;
    }
}
