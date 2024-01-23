package cmc.peerna.web.dto.responseDto;

import cmc.peerna.domain.PeerFeedback;
import cmc.peerna.domain.enums.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberStatusDto{
        private Long memberId;
        private String status;
        private LocalDateTime calledAt;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberSimpleInfoDto{
        private String name;
        private TestType testType;
        private Part part;
        private Job job;

        private Integer totalScore;
        private String oneLiner;
    }


}
