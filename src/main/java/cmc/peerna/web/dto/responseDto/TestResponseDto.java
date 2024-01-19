package cmc.peerna.web.dto.responseDto;

import cmc.peerna.domain.SelfTestResult;
import cmc.peerna.domain.enums.PeerCard;
import cmc.peerna.domain.enums.TestType;
import lombok.*;

import java.util.List;

public class TestResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class selfTestResultResponseDto {

        private String memberName;
        private TestType testType;
        private PeerCard group1;
        private PeerCard group2;
        private PeerCard group3;
        private PeerCard group4;

    }
}
