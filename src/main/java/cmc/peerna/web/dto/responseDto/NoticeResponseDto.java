package cmc.peerna.web.dto.responseDto;

import cmc.peerna.domain.enums.NoticeType;
import lombok.*;

import java.time.LocalDateTime;

public class NoticeResponseDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class noticeSimpleInfoDto{
        private Long projectId;

        // 알림의 좌측 아이콘 구분 위하 필드
        private NoticeType noticeType;
        private String contents;
        private LocalDateTime createdTime;

    }
}
