package cmc.peerna.web.dto.responseDto;

import cmc.peerna.domain.PeerFeedback;
import cmc.peerna.domain.enums.Job;
import cmc.peerna.domain.enums.Part;
import cmc.peerna.domain.enums.PeerCard;
import cmc.peerna.domain.enums.TestType;
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

        // 업무 적극성
        private Long Positiveness;
        private String oneLiner;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MypageDto{
        MemberSimpleInfoDto memberSimpleInfoDto;

        // 내가 생각하는 내 모습
        List<PeerCard> selfTestCardList;
        // 동료들이 생각하는 내 모습
        List<PeerCard> peerCardList;
        // 종합평가 탑3개 (PeerGrade)
        // DTO 새로 만들기!

        // 종합점수
        private int totalScore;
        // 유저 피드백 최신 3개
        List<PeerFeedback> peerFeedbackList;
    }
}
