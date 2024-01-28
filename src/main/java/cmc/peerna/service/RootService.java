package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.web.dto.responseDto.HomeResponseDto;
import cmc.peerna.web.dto.responseDto.RootResponseDto;
import cmc.peerna.web.dto.responseDto.TestResponseDto;

import java.util.List;

public interface RootService {
    List<Long> getMyPageColorAnswerIdList(Member member, List<Long> selfTestAnswerIdList);
    RootResponseDto.MypageDto getMyPageDto(Member member);

    HomeResponseDto.peerDetailPageDto getPeerDetailPageDto(Member me, Member target);

    List<TestResponseDto.totalEvaluation> getTotalEvaluationList(Member member);

    RootResponseDto.AllFeedbackDto getFeedbackList(Member member, Integer page);

    RootResponseDto.memberSimpleDtoPage getMemberListByPeerType(Member member, String testType, Integer pageSize);
    RootResponseDto.memberSimpleDtoPage getMemberListByPart(Member member, String part, Integer pageSize);

    List<Long> getPeerTestAnswerIdList(Member member);
    List<Long> getPeerAndMyCommonAnswerIdList(List<Long> peerAnswerIdList, List<Long> myAnswerIdList);
}

