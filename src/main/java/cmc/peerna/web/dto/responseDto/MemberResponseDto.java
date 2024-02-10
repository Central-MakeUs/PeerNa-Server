package cmc.peerna.web.dto.responseDto;

import cmc.peerna.domain.enums.*;
import lombok.*;

import java.time.LocalDateTime;

public class MemberResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberGetTestDto {
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
    public static class MemberMyPageInfoDto {
        private String name;
        private TestType testType;
        private Part part;
        private Job job;

        private Integer totalScore;
        private String oneLiner;
        private String uuid;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class memberBasicInfoDto{
        private String name;
        private Job job;
        private Part part;
        private String oneLiner;
        private String uuid;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class memberNameResponseDto{
        private String name;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class newTokenDto {
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class memberSimpleProfileDto {
        private Long memberId;
        private String name;
        private Job job;
        private Part part;
        private TestType peerTestType;
        private String oneLiner;
        private Integer totalScore;

    }
}
