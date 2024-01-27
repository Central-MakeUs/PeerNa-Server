package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.web.dto.responseDto.RootResponseDto;
import cmc.peerna.web.dto.responseDto.TestResponseDto;

import java.io.IOException;
import java.util.List;

public interface RootService {
    List<Long> getcolorAnswerIdList(Member member, List<Long> selfTestAnswerIdList);
    RootResponseDto.MypageDto getMyPageDto(Member member);

    List<TestResponseDto.totalEvaluation> getTotalEvaluationList(Member member);

    RootResponseDto.AllFeedbackDto getFeedbackList(Member member, Integer page);

    void testFCMService(String fcmToken) throws IOException;
}

