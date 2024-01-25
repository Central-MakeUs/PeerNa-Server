package cmc.peerna.converter;

import cmc.peerna.domain.PeerFeedback;
import cmc.peerna.domain.SelfTest;
import cmc.peerna.domain.SelfTestResult;
import cmc.peerna.domain.enums.PeerCard;
import cmc.peerna.web.dto.responseDto.TestResponseDto;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TestConverter {

    public static TestResponseDto.selfTestResultResponseDto toSelfTestResultDto(SelfTestResult selfTestResult) {
        return TestResponseDto.selfTestResultResponseDto.builder()
                .memberName(selfTestResult.getMember().getName())
                .testType(selfTestResult.getTestType())
                .group1(selfTestResult.getGroup1())
                .group2(selfTestResult.getGroup2())
                .group3(selfTestResult.getGroup3())
                .group4(selfTestResult.getGroup4())
                .build();
    }

    public static List<PeerCard> selfTestResultToPeerCardList(SelfTestResult selfTestResult) {
        if(selfTestResult==null) return null;
        List<PeerCard> peerCardList = new ArrayList<>();
        peerCardList.add(selfTestResult.getGroup1());
        peerCardList.add(selfTestResult.getGroup2());
        peerCardList.add(selfTestResult.getGroup3());
        peerCardList.add(selfTestResult.getGroup4());
        return peerCardList;
    }

    public static List<Long> selfTestToAnswerId(List<SelfTest> selfTestList) {
        List<Long> answerIdList = new ArrayList<>();
        for (SelfTest selfTest : selfTestList) {
            answerIdList.add(selfTest.getAnswer().getId());
        }
        return answerIdList;
    }

    public static List<String> peerFeedbackListToStringList(List<PeerFeedback> peerFeedbackList) {
        List<String> feedbackContentList = new ArrayList<>();
        for (PeerFeedback peerFeedback : peerFeedbackList) {
            feedbackContentList.add(peerFeedback.getContents());
        }
        return feedbackContentList;
    }
}
