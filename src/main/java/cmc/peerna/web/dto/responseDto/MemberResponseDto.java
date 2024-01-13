package cmc.peerna.web.dto.responseDto;

import lombok.*;

public class MemberResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberBaseDto{
        private Long id;
        private String name;
        private String oneLiner;
    }
}
