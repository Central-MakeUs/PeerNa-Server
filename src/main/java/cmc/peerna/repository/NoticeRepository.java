package cmc.peerna.repository;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.Notice;
import cmc.peerna.domain.enums.NoticeGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Page<Notice> findAllByNoticeGroupAndReceiver(NoticeGroup noticeGroup, Member receiver, PageRequest pageRequest);
}
