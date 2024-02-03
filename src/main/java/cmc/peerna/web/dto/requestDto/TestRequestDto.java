package cmc.peerna.web.dto.requestDto;

import cmc.peerna.domain.enums.PeerGrade;
import lombok.*;

import java.util.List;
import java.util.UUID;

public class TestRequestDto {
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class peerTestRequestDto{
        private List<Long> answerIdList;
        private PeerGrade peerGrade;
        private String feedback;

        private String uuid;
    }


}
