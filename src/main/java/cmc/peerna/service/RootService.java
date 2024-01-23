package cmc.peerna.service;

import cmc.peerna.domain.Member;
import cmc.peerna.web.dto.responseDto.RootResponseDto;
import cmc.peerna.web.dto.responseDto.TestResponseDto;

import java.util.List;

public interface RootService {
    List<Long> getcolorAnswerIdList(Member member, List<Long> selfTestAnswerIdList);
    List<TestResponseDto.totalEvaluation> getTop3TotalEvaluation(Member member);
    RootResponseDto.MypageDto getMyPageDto(Member member);

    List<TestResponseDto.totalEvaluation> getTotalEvaluationList(Member member);
}

