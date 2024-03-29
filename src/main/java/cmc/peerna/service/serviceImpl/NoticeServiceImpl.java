package cmc.peerna.service.serviceImpl;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.apiResponse.exception.handler.MemberException;
import cmc.peerna.apiResponse.exception.handler.NoticeException;
import cmc.peerna.apiResponse.exception.handler.RootException;
import cmc.peerna.converter.NoticeConverter;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.Notice;
import cmc.peerna.domain.enums.NoticeGroup;
import cmc.peerna.domain.enums.NoticeType;
import cmc.peerna.repository.MemberRepository;
import cmc.peerna.repository.NoticeRepository;
import cmc.peerna.service.NoticeService;
import cmc.peerna.web.dto.responseDto.NoticeResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    @Value("${paging.size}")
    private Integer pageSize;

    @Override
    @Transactional
    public void createNotice(Member sender, Long receiverId, NoticeGroup noticeGroup, NoticeType noticeType, Long targetId, String contents) {
        Member receiver = memberRepository.findById(receiverId).orElseThrow(() -> new MemberException(ResponseStatus.MEMBER_NOT_FOUND));
        noticeRepository.save(Notice.builder()
                .sender(sender)
                .receiver(receiver)
                .noticeType(noticeType)
                .noticeGroup(noticeGroup)
                .targetId(targetId)
                .contents(contents)
                .readFlag("false")
                .build()
        );
    }

    @Override
    @Transactional
    public void createProjectRequestNotice(Member sender, Long receiverId, NoticeGroup noticeGroup, NoticeType noticeType, Long projectId, Long senderId, String contents) {
        Member receiver = memberRepository.findById(receiverId).orElseThrow(() -> new MemberException(ResponseStatus.MEMBER_NOT_FOUND));
        noticeRepository.save(Notice.builder()
                .sender(sender)
                .receiver(receiver)
                .noticeType(noticeType)
                .noticeGroup(noticeGroup)
                .targetId(projectId)
                .subTargetId(senderId)
                .contents(contents)
                .readFlag("false")
                .build()
        );
    }

    @Override
    @Transactional
    public void updatePeerTestNoticeDoneStatus(Member sender, Member receiver){
        List<Notice> peerTestNoticeList = noticeRepository.findAllByNoticeTypeAndSenderAndReceiver(NoticeType.PEER_TEST_REQUEST, sender, receiver);

        peerTestNoticeList.stream()
                .map(notice -> {
                    notice.noticeDone();
                    return null;
                }).collect(Collectors.toList());

    }


    @Override
    @Transactional
    public NoticeResponseDto.NoticePageDto getNoticePageByNoticeGroup(Member receiver, NoticeGroup noticeGroup, Integer page) {

        PageRequest pageRequest = PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("createdAt")));
        Page<Notice> noticeByNoticeGroup = noticeRepository.findAllByNoticeGroupAndReceiver(noticeGroup, receiver, pageRequest);
//        if (noticeByNoticeGroup.getTotalElements() == 0L) {
//            throw new RootException(ResponseStatus.NOTICE_COUNT_ZERO);
//        }
//        if (noticeByNoticeGroup.getTotalPages() <= page)
//            throw new MemberException(ResponseStatus.OVER_PAGE_INDEX_ERROR);

        NoticeResponseDto.NoticePageDto noticePageDto = NoticeConverter.toNoticePageDto(noticeByNoticeGroup);

        List<Notice> noticeList = noticeByNoticeGroup.getContent();
        noticeList.stream()
                .map(notice -> {
                    notice.noticeRead();
                    return null;
                }).collect(Collectors.toList());


        return noticePageDto;
    }

    @Override
    @Transactional
    public NoticeResponseDto.NoticePageDto getProjectNoticePage(Member receiver, Integer page) {
        return getNoticePageByNoticeGroup(receiver, NoticeGroup.PROJECT, page);
    }

    @Override
    @Transactional
    public NoticeResponseDto.NoticePageDto getPeerTestNoticePage(Member receiver, Integer page) {
        return getNoticePageByNoticeGroup(receiver, NoticeGroup.PEER_TEST, page);
    }

    @Override
    public boolean existsNotice(Long receiverId, Long senderId, NoticeType noticeType) {
        return noticeRepository.existsByReceiverIdAndSenderIdAndNoticeType(receiverId, senderId, noticeType);
    }

    @Override
    public void existsProjectJoinRequestNotice(Long receiverId, Long senderId) {
        if(!existsNotice(receiverId, senderId, NoticeType.REQUEST_JOIN_PROJECT)){
            throw new NoticeException(ResponseStatus.PROJECT_REQUEST_NOTICE_NOT_FOUND);
        }
    }

    @Override
    public void existsPeerTestRequestNotice(Long receiverId, Long senderId) {
        if (!noticeRepository.existsByReceiverIdAndSenderIdAndNoticeType(receiverId, senderId, NoticeType.PEER_TEST_REQUEST)) {
            throw new NoticeException(ResponseStatus.PEER_TEST_REQUEST_NOTICE_NOT_FOUND);
        }
    }

}
