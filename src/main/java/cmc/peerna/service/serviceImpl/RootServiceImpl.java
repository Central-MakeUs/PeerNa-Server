package cmc.peerna.service.serviceImpl;

import cmc.peerna.converter.MemberConverter;
import cmc.peerna.converter.TestConverter;
import cmc.peerna.domain.*;
import cmc.peerna.domain.enums.PeerCard;
import cmc.peerna.domain.enums.PeerGrade;
import cmc.peerna.domain.enums.TestType;
import cmc.peerna.repository.*;
import cmc.peerna.service.RootService;
import cmc.peerna.utils.TestResultCalculator;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import cmc.peerna.web.dto.responseDto.RootResponseDto;
import cmc.peerna.web.dto.responseDto.TestResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RootServiceImpl implements RootService {
    private final SelfTestRepository selfTestRepository;
    private final SelfTestResultRepository selfTestResultRepository;
    private final PeerFeedbackRepository peerFeedbackRepository;
    private final PeerGradeResultRepository peerGradeResultRepository;
    private final PeerTestRepository peerTestRepository;
    private final TestResultCalculator testResultCalculator;

    @Override
    public List<Long> getcolorAnswerIdList(Member member, List<Long> selfTestAnswerIdList) {
        List<Long> peerTestAnswerIdList = new ArrayList<>();
        List<Long> colorAnswerIdList = new ArrayList<>();
        for (Long i = 1L; i <= 36L; i+=2) {
            Long answerA = peerTestRepository.countByTargetAndAnswerId(member, i);
            Long answerB = peerTestRepository.countByTargetAndAnswerId(member, i + 1);
            if (answerA > answerB) {
                peerTestAnswerIdList.add(i);
            } else if (answerA < answerB) {
                peerTestAnswerIdList.add(i + 1);
            } else{
                peerTestAnswerIdList.add(i);
                peerTestAnswerIdList.add(i+1);
            }
        }

        for (Long answerId : peerTestAnswerIdList) {
            if (selfTestAnswerIdList.contains(answerId)) {
                colorAnswerIdList.add(answerId);
            }
        }

        return colorAnswerIdList;
    }

    @Override
    public List<TestResponseDto.totalEvaluation> getTop3TotalEvaluation(Member member) {
        List<PeerGrade> gradeList = Arrays.asList(PeerGrade.values());
        List<TestResponseDto.totalEvaluation> totalEvaluationList = new ArrayList<>();
        for (PeerGrade peerGrade : gradeList) {
            Long count = peerGradeResultRepository.countByTargetAndPeerGrade(member, peerGrade);
            totalEvaluationList.add(TestResponseDto.totalEvaluation.builder()
                    .count(count)
                    .peerGrade(peerGrade)
                    .build()
            );
        }
        totalEvaluationList.sort(Comparator.comparing(TestResponseDto.totalEvaluation::getCount).reversed());
        return totalEvaluationList.subList(0, 3);
    }

    @Override
    public RootResponseDto.MypageDto getMyPageDto(Member member) {
        boolean peerTestMoreThanThree = peerGradeResultRepository.countByTarget(member) >= 3 ? true : false;

        MemberResponseDto.MemberSimpleInfoDto memberSimpleInfoDto = MemberConverter.toSimpleInfoDto(member);

        SelfTestResult selfTestResult = selfTestResultRepository.findByMember(member);

        List<PeerTest> peerTestList = peerTestRepository.findALlByTarget(member);

        TestType peerTestType = testResultCalculator.peerTestPeerType(peerTestList);

        List<PeerCard> selfTestCardList = TestConverter.selfTestResultToPeerCardList(selfTestResult);

        List<PeerCard> peerCardList = testResultCalculator.getPeerTestPeerCard(peerTestList);

        List<TestResponseDto.totalEvaluation> top3TotalEvaluation = getTop3TotalEvaluation(member);

        List<PeerGradeResult> peerGradeResultList = peerGradeResultRepository.findAllByTarget(member);

        List<PeerFeedback> peerFeedbackList = peerFeedbackRepository.findTop3ByTargetOrderByCreatedAtDesc(member);

        List<Long> selfTestAnswerIdList = TestConverter.selfTestToAnswerId(selfTestRepository.findALlByWriter(member));

        List<Long> colorAnswerIdList = getcolorAnswerIdList(member, selfTestAnswerIdList);

        return RootResponseDto.MypageDto.builder()
                .peerTestMoreThanThree(peerTestMoreThanThree)
                .memberSimpleInfoDto(memberSimpleInfoDto)
                .peerTestType(peerTestType)
                .selfTestCardList(selfTestCardList)
                .peerCardList(peerCardList)
                .top3Evaluation(top3TotalEvaluation)
                .totalScore(member.getTotalScore())
                .peerFeedbackList(TestConverter.peerFeedbackListToStringList(peerFeedbackList))
                .selfTestAnswerIdList(selfTestAnswerIdList)
                .colorAnswerIdList(colorAnswerIdList)
                .build();

    }
}
