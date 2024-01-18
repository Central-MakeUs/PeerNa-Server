package cmc.peerna.service.serviceImpl;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.TestException;
import cmc.peerna.domain.Answer;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.Question;
import cmc.peerna.domain.SelfTest;
import cmc.peerna.repository.AnswerRepository;
import cmc.peerna.repository.SelfTestRepository;
import cmc.peerna.service.TestService;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private static Long answerCount = 18L;
    private final AnswerRepository answerRepository;
    private final SelfTestRepository selfTestRepository;
    public void saveSelfTest(Member member, MemberRequestDto.selfTestDto request) {
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
}
