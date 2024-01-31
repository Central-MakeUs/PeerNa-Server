package cmc.peerna.service.serviceImpl;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.Notice;
import cmc.peerna.domain.enums.NoticeType;
import cmc.peerna.repository.MemberRepository;
import cmc.peerna.repository.NoticeRepository;
import cmc.peerna.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void createNotice(Member sender, Long receiverId, NoticeType noticeType) {
        Member receiver = memberRepository.findById(receiverId).orElseThrow(() -> new MemberException(ResponseStatus.MEMBER_NOT_FOUND));
        noticeRepository.save(Notice.builder()
                .sender(sender)
                .receiver(receiver)
                .noticeType(noticeType)
                .build()
        );
    }
}
