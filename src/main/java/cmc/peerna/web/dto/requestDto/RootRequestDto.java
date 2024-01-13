package cmc.peerna.web.dto.requestDto;

import lombok.*;

public class RootRequestDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostTestDto{
        private String body;
    }
}
