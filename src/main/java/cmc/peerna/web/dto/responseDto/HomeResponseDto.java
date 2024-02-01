package cmc.peerna.web.dto.responseDto;

import cmc.peerna.domain.enums.PeerCard;
import cmc.peerna.domain.enums.TestType;
import lombok.*;

import java.util.List;

public class HomeResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class peerDetailPageDto{

        private boolean peerTestMoreThanThree;
        MemberResponseDto.memberSimpleProfileDto memberSimpleProfileDto;

        private List<PeerCard> peerCardList;

        private List<PeerCard> myCardList;

        private List<TestResponseDto.totalEvaluation> totalEvaluation;

        private Integer totalScore;

        List<String> peerFeedbackList;

        private List<Long> peerAnswerIdList;

        // 나와 동료의 공통점
        private List<Long> colorAnswerIdList;

        // 해당 동료가 참여중인 프로젝트 최신순 3개
        private List<ProjectResponseDto.ProjectSimpleProfileDto> peerProjectDtoList;

    }
}
