package cmc.peerna.web.dto.responseDto;

import cmc.peerna.domain.enums.PeerCard;
import cmc.peerna.domain.enums.TestType;
import lombok.*;

import java.util.List;

public class RootResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MypageDto{
        private boolean peerTestMoreThanThree;
        private Long peerTestCount;
        private MemberResponseDto.MemberMyPageInfoDto memberMyPageInfoDto;

        private TestType peerTestType;

        // 내가 생각하는 내 모습
        private List<PeerCard> selfTestCardList;
        // 동료들이 생각하는 내 모습
        private List<PeerCard> peerCardList;

        private List<TestResponseDto.totalEvaluation> totalEvaluation;

        // 종합점수
        private Integer totalScore;

        // 유저 피드백 최신 3개
        List<String> peerFeedbackList;

        private List<Long> selfTestAnswerIdList;
        private List<Long> colorAnswerIdList;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class TotalEvaluationSeeMoreDto{
        private List<TestResponseDto.totalEvaluation> totalEvaluationList;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class AllFeedbackDto{
        List<String> feedbackList;
        Long totalElements;
        Integer currentPageElements;
        Integer totalPage;
        Boolean isFirst;
        Boolean isLast;
    }


    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class memberSimpleDtoPage {
        List<MemberResponseDto.memberSimpleProfileDto> memberSimpleProfileDtoList;
        Long totalElements;
        Integer currentPageElements;
        Integer totalPage;
        Boolean isFirst;
        Boolean isLast;
    }
}
