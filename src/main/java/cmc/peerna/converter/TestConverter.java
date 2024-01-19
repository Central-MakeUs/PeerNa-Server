package cmc.peerna.converter;

import cmc.peerna.domain.SelfTestResult;
import cmc.peerna.web.dto.responseDto.TestResponseDto;
import lombok.RequiredArgsConstructor;

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

}
