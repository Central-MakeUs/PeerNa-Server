package cmc.peerna.web.dto.requestDto;

import cmc.peerna.domain.enums.TestType;
import lombok.*;

public class RootRequestDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostTestDto{
        private String body;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class FCMTestDto{
        String fcmToken;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PeerTypeDto{
        TestType peerType;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PageRequestDto{
        Long totalElements;
        Integer currentPageElements;
        Integer totalPage;
        Boolean isFirst;
        Boolean isLast;
    }
}
