package cmc.peerna.service.serviceImpl;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.apiResponse.exception.handler.RootException;
import cmc.peerna.converter.MemberConverter;
import cmc.peerna.converter.ProjectConverter;
import cmc.peerna.converter.TestConverter;
import cmc.peerna.domain.*;
import cmc.peerna.domain.enums.Part;
import cmc.peerna.domain.enums.PeerCard;
import cmc.peerna.domain.enums.PeerGrade;
import cmc.peerna.domain.enums.TestType;
import cmc.peerna.domain.mapping.ProjectMember;
import cmc.peerna.fcm.service.FcmService;
import cmc.peerna.repository.*;
import cmc.peerna.service.RootService;
import cmc.peerna.utils.TestResultCalculator;
import cmc.peerna.web.dto.responseDto.*;
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

    private final ProjectMemberRepository projectMemberRepository;

    private final TestResultCalculator testResultCalculator;
    private final FcmService fcmService;
    @Value("${paging.size}")
    private Integer pageSize;

    @Override
    public List<Long> getMyPageColorAnswerIdList(Member member, List<Long> selfTestAnswerIdList) {
        List<Long> peerTestAnswerIdList = new ArrayList<>();
        List<Long> colorAnswerIdList = new ArrayList<>();
        for (Long i = 1L; i <= 36L; i += 2) {
            Long answerA = peerTestRepository.countByTargetAndAnswerId(member, i);
            Long answerB = peerTestRepository.countByTargetAndAnswerId(member, i + 1);
            if (answerA > answerB) {
                peerTestAnswerIdList.add(i);
            } else if (answerA < answerB) {
                peerTestAnswerIdList.add(i + 1);
            } else {
                peerTestAnswerIdList.add(i);
                peerTestAnswerIdList.add(i + 1);
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
    public List<Long> getPeerTestAnswerIdList(Member member) {
        List<Long> peerTestAnswerIdList = new ArrayList<>();
        for (Long i = 1L; i <= 36L; i += 2) {
            Long answerA = peerTestRepository.countByTargetAndAnswerId(member, i);
            Long answerB = peerTestRepository.countByTargetAndAnswerId(member, i + 1);
            if (answerA >= answerB) {
                peerTestAnswerIdList.add(i);
            } else {
                peerTestAnswerIdList.add(i + 1);
            }
        }

        return peerTestAnswerIdList;
    }

    @Override
    public List<Long> getPeerAndMyCommonAnswerIdList(List<Long> peerAnswerIdList, List<Long> myAnswerIdList) {
        peerAnswerIdList.retainAll(myAnswerIdList);
        return peerAnswerIdList;
    }


    @Override
    public List<TestResponseDto.totalEvaluation> getTotalEvaluationList(Member member) {
        List<PeerGrade> gradeList = Arrays.asList(PeerGrade.values());
        List<TestResponseDto.totalEvaluation> totalEvaluationList = new ArrayList<>();
        for (PeerGrade peerGrade : gradeList) {
            Long count = peerGradeResultRepository.countByTargetAndPeerGrade(member, peerGrade);
            if (count == 0L) continue;
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
        Long peerTestCount = peerGradeResultRepository.countByTarget(member);
        boolean peerTestMoreThanThree = peerTestCount >= 3 ? true : false;

        MemberResponseDto.MemberMyPageInfoDto memberMyPageInfoDto = MemberConverter.toSimpleInfoDto(member);

        SelfTestResult selfTestResult = selfTestResultRepository.findByMember(member);

        List<PeerCard> selfTestCardList = TestConverter.selfTestResultToPeerCardList(selfTestResult);
        List<Long> selfTestAnswerIdList = TestConverter.selfTestToAnswerId(selfTestRepository.findAllByWriter(member));

        List<PeerTest> peerTestList = new ArrayList<>();
        List<PeerCard> peerCardList = new ArrayList<>();
        List<TestResponseDto.totalEvaluation> totalEvaluation = new ArrayList<>();
        List<PeerFeedback> peerFeedbackList = new ArrayList<>();

        List<Long> colorAnswerIdList = new ArrayList<>();

        if(peerTestMoreThanThree==true){
            peerTestList = peerTestRepository.findALlByTarget(member);
            peerCardList = testResultCalculator.getPeerTestPeerCard(peerTestList);
            totalEvaluation = getTotalEvaluationList(member);
            peerFeedbackList = peerFeedbackRepository.findTop3ByTargetOrderByCreatedAtDesc(member);
            colorAnswerIdList = getMyPageColorAnswerIdList(member, selfTestAnswerIdList);
        }


        return RootResponseDto.MypageDto.builder()
                .peerTestMoreThanThree(peerTestMoreThanThree)
                .peerTestCount(peerTestCount)
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
    public HomeResponseDto.peerDetailPageDto getPeerDetailPageDto(Member me, Member target){
        boolean peerTestMoreThanThree = peerGradeResultRepository.countByTarget(target) >= 3 ? true : false;

        MemberResponseDto.memberSimpleProfileDto memberSimpleProfileDto = MemberConverter.toMemberSimpleProfileDto(target);


        List<PeerTest> myPeerTestList = new ArrayList<>();
        List<PeerCard> myPeerCardList = new ArrayList<>();

        List<PeerTest> peerPeerTestList = new ArrayList<>();
        List<PeerCard> peerCardList = new ArrayList<>();
        List<TestResponseDto.totalEvaluation> totalEvaluation = new ArrayList<>();
        List<PeerFeedback> peerFeedbackList = new ArrayList<>();
        List<Long> peerAnswerIdList = new ArrayList<>();
        List<Long> myAnswerIdList = new ArrayList<>();
        List<Long> colorAnswerIdList = new ArrayList<>();

        if (peerGradeResultRepository.countByTarget(me) >= 3) {
            myPeerTestList = peerTestRepository.findALlByTarget(me);
            myPeerCardList = testResultCalculator.getPeerTestPeerCard(myPeerTestList);
            myAnswerIdList = getPeerTestAnswerIdList(me);
        }



        if (peerTestMoreThanThree == true) {
            peerPeerTestList = peerTestRepository.findALlByTarget(target);
            peerCardList = testResultCalculator.getPeerTestPeerCard(peerPeerTestList);
            totalEvaluation = getTotalEvaluationList(target);
            peerFeedbackList = peerFeedbackRepository.findTop3ByTargetOrderByCreatedAtDesc(target);
            peerAnswerIdList = getPeerTestAnswerIdList(target);
            if(peerGradeResultRepository.countByTarget(me) >= 3) {
                colorAnswerIdList = getPeerAndMyCommonAnswerIdList(peerAnswerIdList, myAnswerIdList);
            }

        }

        // 동료의 피어카드 리스트
//        List<PeerTest> peerPeerTestList = peerTestRepository.findALlByTarget(target);
//        List<PeerCard> peerCardList = testResultCalculator.getPeerTestPeerCard(peerPeerTestList);

        // 나의 피어카드 리스트
//        List<PeerTest> myPeerTestList = peerTestRepository.findALlByTarget(me);
//        List<PeerCard> myPeerCardList = testResultCalculator.getPeerTestPeerCard(myPeerTestList);

//        List<TestResponseDto.totalEvaluation> totalEvaluation = getTotalEvaluationList(target);
//
//        List<PeerFeedback> peerFeedbackList = peerFeedbackRepository.findTop3ByTargetOrderByCreatedAtDesc(target);

//        List<Long> peerAnswerIdList = getPeerTestAnswerIdList(target);
//        List<Long> myAnswerIdList = getPeerTestAnswerIdList(me);
//
//        List<Long> colorAnswerIdList = getPeerAndMyCommonAnswerIdList(peerAnswerIdList, myAnswerIdList);



        List<Project> top3projectList = projectMemberRepository.qFindProjectByMemberOrderByCreatedAtDesc(target);
        if (top3projectList.size() > 3) {
            top3projectList = top3projectList.subList(0, 3);
        }
        List<ProjectResponseDto.ProjectSimpleProfileDto> top3ProjectSimpleProfileList = ProjectConverter.toProjectSimpleProfileList(top3projectList);

        return HomeResponseDto.peerDetailPageDto.builder()
                .peerTestMoreThanThree(peerTestMoreThanThree)
                .myName(me.getName())
                .memberSimpleProfileDto(memberSimpleProfileDto)
                .peerCardList(peerCardList)
                .myCardList(myPeerCardList)
                .totalEvaluation(totalEvaluation)
                .totalScore(target.getTotalScore())
                .peerFeedbackList(TestConverter.peerFeedbackListToStringList(peerFeedbackList))
                .peerAnswerIdList(peerAnswerIdList)
                .colorAnswerIdList(colorAnswerIdList)
                .peerProjectDtoList(top3ProjectSimpleProfileList)
                .build();
    }
    @Override
    public RootResponseDto.AllFeedbackDto getFeedbackList(Member member, Integer page) {
//        if (!peerFeedbackRepository.existsByTarget(member)) return null;
        Page<PeerFeedback> peerFeedbacks = peerFeedbackRepository.findAllByTarget(member, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
//        if (peerFeedbacks.getTotalPages() <= page)
//            throw new MemberException(ResponseStatus.OVER_PAGE_INDEX_ERROR);
        RootResponseDto.AllFeedbackDto allFeedbackDto = MemberConverter.toFeedbackString(peerFeedbacks, member);
        return allFeedbackDto;
    }

    @Override
    public ProjectResponseDto.ProjectPageDto getPeerProject(Member member, Integer page) {
        Page<Project> projectPage = projectMemberRepository.qFindProjectPageByMemberOrderByCreatedAtDesc(member, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "createdAt")));
//        if (projectPage.getTotalPages() <= page)
//            throw new MemberException(ResponseStatus.OVER_PAGE_INDEX_ERROR);

        ProjectResponseDto.ProjectPageDto projectPageDto = ProjectConverter.toProjectPageDto(projectPage);
        return projectPageDto;
    }

    @Override
    public RootResponseDto.memberSimpleDtoPage getMemberListByPeerType(Member member, String testType, Integer page) {

        if (!TestType.isValidTestType(testType)) {
            throw new RootException(ResponseStatus.WRONG_TEST_TYPE);
        }

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("totalScore"), Sort.Order.asc("name")));
        Page<Member> memberByPeerTypePage = memberRepository.findAllByPeerTestTypeAndIdNot(TestType.valueOf(testType), member.getId(), pageRequest);
//        if (memberByPeerTypePage.getTotalElements() == 0L) {
//            throw new RootException(ResponseStatus.MEMBER_COUNT_ZERO);
//        }
//        if (memberByPeerTypePage.getTotalPages() <= page)
//            throw new MemberException(ResponseStatus.OVER_PAGE_INDEX_ERROR);
        RootResponseDto.memberSimpleDtoPage memberByPeerTypeDto = MemberConverter.toSearchByPeerTypeDto(memberByPeerTypePage);
        return memberByPeerTypeDto;
    }

    @Override
    public RootResponseDto.memberSimpleDtoPage getMemberListByPart(Member member, String part, Integer page) {
        if (!Part.isValidPart(part) && !part.equals("ALL")) {
            throw new RootException(ResponseStatus.WRONG_PART);
        }

        Page<Member> memberByPeerTypePage;

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("totalScore"), Sort.Order.asc("name")));

        if (part.equals("ALL")) {
            memberByPeerTypePage = memberRepository.findAllByIdNotAndPartNotNull(member.getId(), pageRequest);
        } else {
            memberByPeerTypePage = memberRepository.findAllByPartAndIdNot(Part.valueOf(part), member.getId(), pageRequest);
        }

//        if (memberByPeerTypePage.getTotalElements() == 0L) {
//            throw new RootException(ResponseStatus.MEMBER_COUNT_ZERO);
//        }
//        if (memberByPeerTypePage.getTotalPages() <= page)
//            throw new MemberException(ResponseStatus.OVER_PAGE_INDEX_ERROR);
        RootResponseDto.memberSimpleDtoPage memberByPeerTypeDto = MemberConverter.toSearchByPeerTypeDto(memberByPeerTypePage);
        return memberByPeerTypeDto;
    }
}
