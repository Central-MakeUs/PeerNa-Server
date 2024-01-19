package cmc.peerna.utils;

import cmc.peerna.domain.SelfTest;
import cmc.peerna.domain.enums.AnswerChoice;
import cmc.peerna.domain.enums.PeerCard;
import cmc.peerna.domain.enums.TestType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SelfTestResultCalculator {

    public TestType selfTestPeerType(List<SelfTest> selfTestList) {
        Long DCount = 0L;
        Long ICount = 0L;
        Long SCount = 0L;
        Long CCount = 0L;

        for (SelfTest selfTest : selfTestList) {
            TestType testType = selfTest.getAnswer().getTestType();
            switch (testType) {
                case D:
                    DCount++;
                    break;
                case I:
                    ICount++;
                    break;
                case S:
                    SCount++;
                    break;
                case C:
                    CCount++;
                    break;
            }
        }

        List<Long> counts = Arrays.asList(DCount, ICount, SCount, CCount);
        Long max = Collections.max(counts);
        if (DCount == max) {
            return TestType.D;
        } else if (ICount == max) {
            return TestType.I;
        } else if (SCount == max) {
            return TestType.S;
        } else
            return TestType.C;
    }
    public List<PeerCard> selfTestPeerCard(List<SelfTest> selfTestList) {
        List<PeerCard> peerCardList = new ArrayList<>(4);

        Long group1ACount = 0L;
        Long group2ACount = 0L;
        Long group3ACount = 0L;
        Long group4ACount = 0L;

        for (SelfTest selfTest : selfTestList) {
            if (selfTest.getQuestion().getId() <= 5) {
                if (selfTest.getAnswer().getAnswerChoice().equals(AnswerChoice.A)) {
                    group1ACount++;
                }
            } else if (selfTest.getQuestion().getId() <= 9) {
                if (selfTest.getAnswer().getAnswerChoice().equals(AnswerChoice.A)) {
                    group2ACount++;
                }
            } else if (selfTest.getQuestion().getId() <= 13) {
                if (selfTest.getAnswer().getAnswerChoice().equals(AnswerChoice.A)) {
                    group3ACount++;
                }
            } else if (selfTest.getQuestion().getId() <= 18) {
                if (selfTest.getAnswer().getAnswerChoice().equals(AnswerChoice.A)) {
                    group4ACount++;
                }
            }
        }

        if (group1ACount >= 3) {
            peerCardList.add(PeerCard.DRIVING);
        } else if (group1ACount <= 2) {
            peerCardList.add(PeerCard.COOPERATIVE);
        }

        if (group2ACount > 2) {
            peerCardList.add(PeerCard.ANALYTICAL);
        } else if (group2ACount == 2) {
            peerCardList.add(PeerCard.COMPREHENSIVE);
        } else if (group2ACount < 2) {
            peerCardList.add(PeerCard.FUTURE_ORIENTED);
        }

        if (group3ACount > 2) {
            peerCardList.add(PeerCard.PRAGMATIC);
        } else if (group3ACount == 2) {
            peerCardList.add(PeerCard.MULTIDIMENSIONAL);
        } else if (group3ACount < 2) {
            peerCardList.add(PeerCard.WARMHEARTED);
        }

        if (group4ACount >= 3) {
            peerCardList.add(PeerCard.CAUTIOUS);
        } else if (group4ACount <= 2) {
            peerCardList.add(PeerCard.CHALLENGING);
        }
        return peerCardList;
    }

}
