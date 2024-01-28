package cmc.peerna.service.serviceImpl;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.apiResponse.exception.handler.RootException;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.converter.TestConverter;
import cmc.peerna.domain.*;
import cmc.peerna.domain.enums.PeerCard;
import cmc.peerna.domain.enums.PeerGrade;
import cmc.peerna.domain.enums.TestType;
import cmc.peerna.fcm.service.FcmService;
import cmc.peerna.repository.*;
import cmc.peerna.service.RootService;
import cmc.peerna.utils.TestResultCalculator;
import cmc.peerna.web.dto.responseDto.MemberResponseDto;
import cmc.peerna.web.dto.responseDto.RootResponseDto;
import cmc.peerna.web.dto.responseDto.TestResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final MemberRepository memberRepository;
    private final PushAlarmRepository pushAlarmRepository;
    private final TestResultCalculator testResultCalculator;
    private final FcmService fcmService;
    @Value("${paging.size}")
    private Integer pageSize;
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
    public List<TestResponseDto.totalEvaluation> getTotalEvaluationList(Member member) {
        List<PeerGrade> gradeList = Arrays.asList(PeerGrade.values());
        List<TestResponseDto.totalEvaluation> totalEvaluationList = new ArrayList<>();
        for (PeerGrade peerGrade : gradeList) {
            Long count = peerGradeResultRepository.countByTargetAndPeerGrade(member, peerGrade);
            if(count==0L) continue;
            totalEvaluationList.add(TestResponseDto.totalEvaluation.builder()
                    .count(count)
                    .peerGrade(peerGrade)
                    .build()
            );
        }
        totalEvaluationList.sort(Comparator.comparing(TestResponseDto.totalEvaluation::getCount).reversed());
        return totalEvaluationList;
    }


    @Override
    public RootResponseDto.MypageDto getMyPageDto(Member member) {
        boolean peerTestMoreThanThree = peerGradeResultRepository.countByTarget(member) >= 3 ? true : false;

        MemberResponseDto.MemberMyPageInfoDto memberMyPageInfoDto = MemberConverter.toSimpleInfoDto(member);

        SelfTestResult selfTestResult = selfTestResultRepository.findByMember(member);

        List<PeerCard> selfTestCardList = TestConverter.selfTestResultToPeerCardList(selfTestResult);

        List<PeerTest> peerTestList = peerTestRepository.findALlByTarget(member);

        List<PeerCard> peerCardList = testResultCalculator.getPeerTestPeerCard(peerTestList);

        List<TestResponseDto.totalEvaluation> totalEvaluation = getTotalEvaluationList(member);

        List<PeerFeedback> peerFeedbackList = peerFeedbackRepository.findTop3ByTargetOrderByCreatedAtDesc(member);

        List<Long> selfTestAnswerIdList = TestConverter.selfTestToAnswerId(selfTestRepository.findALlByWriter(member));

        List<Long> colorAnswerIdList = getcolorAnswerIdList(member, selfTestAnswerIdList);

        return RootResponseDto.MypageDto.builder()
                .peerTestMoreThanThree(peerTestMoreThanThree)
                .memberMyPageInfoDto(memberMyPageInfoDto)
                .peerTestType(member.getPeerTestType())
                .selfTestCardList(selfTestCardList)
                .peerCardList(peerCardList)
                .totalEvaluation(totalEvaluation)
                .totalScore(member.getTotalScore())
                .peerFeedbackList(TestConverter.peerFeedbackListToStringList(peerFeedbackList))
                .selfTestAnswerIdList(selfTestAnswerIdList)
                .colorAnswerIdList(colorAnswerIdList)
                .build();
    }

    @Override
    public RootResponseDto.AllFeedbackDto getFeedbackList(Member member, Integer page) {
        if(!peerFeedbackRepository.existsByTarget(member)) return null;
        Page<PeerFeedback> peerFeedbacks = peerFeedbackRepository.findAllByTarget(member, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
        if(peerFeedbacks.getTotalPages() <= page)
            throw new MemberException(ResponseStatus.OVER_PAGE_INDEX_ERROR);
        RootResponseDto.AllFeedbackDto allFeedbackDto = MemberConverter.toFeedbackString(peerFeedbacks, member);
        return allFeedbackDto;
    }

    @Override
    public RootResponseDto.SearchByPeerTypeDto getMemberListByPeerType(Member member, String testType, Integer page) {

        if (!TestType.isValidTestType(testType)) {
            throw new RootException(ResponseStatus.WRONG_TEST_TYPE);
        }

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("totalScore"), Sort.Order.asc("name")));
        Page<Member> memberByPeerTypePage = memberRepository.findAllByPeerTestTypeAndIdNot(TestType.valueOf(testType), member.getId(), pageRequest);
        if(memberByPeerTypePage.getTotalElements()==0L){
            throw new RootException(ResponseStatus.MEMBER_COUNT_ZERO);
        }
        if(memberByPeerTypePage.getTotalPages() <= page)
            throw new MemberException(ResponseStatus.OVER_PAGE_INDEX_ERROR);
        RootResponseDto.SearchByPeerTypeDto memberByPeerTypeDto = MemberConverter.toSearchByPeerTypeDto(memberByPeerTypePage);
        return memberByPeerTypeDto;
    }

}
