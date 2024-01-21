package cmc.peerna.service.serviceImpl;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.TestException;
import cmc.peerna.converter.TestConverter;
import cmc.peerna.domain.*;
import cmc.peerna.domain.enums.PeerCard;
import cmc.peerna.domain.enums.PeerGrade;
import cmc.peerna.domain.enums.TestType;
import cmc.peerna.repository.*;
import cmc.peerna.service.TestService;
import cmc.peerna.utils.TestResultCalculator;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import cmc.peerna.web.dto.requestDto.TestRequestDto;
import cmc.peerna.web.dto.responseDto.TestResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private static Long answerCount = 18L;
    private final AnswerRepository answerRepository;
    private final SelfTestRepository selfTestRepository;
    private final SelfTestResultRepository selfTestResultRepository;
    private final PeerFeedbackRepository peerFeedbackRepository;
    private final PeerGradeResultRepository peerGradeResultRepository;
    private final PeerTestRepository peerTestRepository;

    private final TestResultCalculator testResultCalculator;

    @Transactional
    @Override
    public void saveSelfTest(Member member, MemberRequestDto.selfTestDto request) {
        if (selfTestRepository.existsByWriter(member)) {
            deleteSelfTest(member);
        }

        List<Long> answerIdList = request.getAnswerIdList();
        if(answerIdList.size()!=answerCount)
            throw new TestException(ResponseStatus.WRONG_ANSWER_COUNT);

        answerIdList.stream()
                .map(answerId ->
                        {
                            Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new TestException(ResponseStatus.ANSWER_NOT_FOUND));
                            Question question = answer.getQuestion();
                            return selfTestRepository.save(
                                    SelfTest.builder()
                                            .writer(member)
                                            .question(question)
                                            .answer(answer)
                                            .build());
                        }
                ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSelfTest(Member member) {
        selfTestRepository.deleteAllByWriter(member);

    }

    @Override
    @Transactional
    public TestResponseDto.selfTestResultResponseDto saveAndGetSelfTestResult(Member member) {
        if(selfTestResultRepository.existsByMember(member)) {
            deleteSelfTestResult(member);
        }

        List<SelfTest> selfTestList = selfTestRepository.findALlByWriter(member);
        TestType peerType = testResultCalculator.selfTestPeerType(selfTestList);
        List<PeerCard> peerCards = testResultCalculator.getSelfTestCard(selfTestList);

        SelfTestResult selfTestResult = SelfTestResult.builder()
                .member(member)
                .testType(peerType)
                .group1(peerCards.get(0))
                .group2(peerCards.get(1))
                .group3(peerCards.get(2))
                .group4(peerCards.get(3))
                .build();
        selfTestResultRepository.save(selfTestResult);
        member.setTestType(peerType);

        return TestConverter.toSelfTestResultDto(selfTestResult);
    }

    @Override
    public TestResponseDto.selfTestResultResponseDto getSelfTestResult(Member member) {
        SelfTestResult selfTestResult = selfTestResultRepository.findByMember(member);
        return TestConverter.toSelfTestResultDto(selfTestResult);
    }

    @Override
    @Transactional
    public void deleteSelfTestResult(Member member) {
        selfTestResultRepository.deleteByMember(member);
    }


    @Override
    @Transactional
    public void savePeerTest(Member writer, Member target, TestRequestDto.peerTestRequestDto request) {

        List<Long> answerIdList = request.getAnswerIdList();
        if(answerIdList.size()!=answerCount)
            throw new TestException(ResponseStatus.WRONG_ANSWER_COUNT);

        answerIdList.stream()
                .map(answerId ->
                        {
                            Answer answer = answerRepository.findById(answerId).orElseThrow(() -> new TestException(ResponseStatus.ANSWER_NOT_FOUND));
                            Question question = answer.getQuestion();
                            return peerTestRepository.save(
                                    PeerTest.builder()
                                            .writer(writer)
                                            .target(target)
                                            .question(question)
                                            .answer(answer)
                                            .nonMemberUuid(request.getUuid().toString())
                                            .build());
                        }
                ).collect(Collectors.toList());

        savePeerFeedBack(writer, target, request.getFeedback(), request.getUuid());
        savePeerGradeResult(writer, target, request.getPeerGrade(), request.getUuid());

    }

    @Override
    @Transactional
    public void savePeerFeedBack(Member writer, Member target, String feedback, String uuid) {
        peerFeedbackRepository.save(PeerFeedback.builder()
                .writer(writer)
                .target(target)
                .contents(feedback)
                .nonMemberUuid(uuid.toString())
                .build()
        );
    }
    @Override
    @Transactional
    public void savePeerGradeResult(Member writer, Member target, PeerGrade peerGrade, String uuid) {
        peerGradeResultRepository.save(PeerGradeResult.builder()
                .writer(writer)
                .target(target)
                .peerGrade(peerGrade)
                .nonMemberUuid(uuid.toString())
                .build()
        );
    }

    @Override
    @Transactional
    public void updatePeerTestMemberId(Member member, String uuid) {

        PeerFeedback peerFeedback = peerFeedbackRepository.findByNonMemberUuid(uuid);
        peerFeedback.updateWriter(member);

        PeerGradeResult peerGradeResult = peerGradeResultRepository.findByNonMemberUuid(uuid);
        peerGradeResult.updateWriter(member);

        List<PeerTest> peerTestList = peerTestRepository.findAllByNonMemberUuid(uuid);

        for (PeerTest peerTest : peerTestList) {
            peerTest.updateWriter(member);
        }
    }

    @Override
    @Transactional
    public List<PeerCard> getPeerTestCard(Member target) {
        List<PeerTest> peerTestList = peerTestRepository.findALlByTarget(target);
        List<PeerCard> peerCardList = testResultCalculator.getPeerTestPeerCard(peerTestList);
        return peerCardList;
    }


}


















