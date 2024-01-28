package cmc.peerna.repository;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.PushAlarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PushAlarmRepository extends JpaRepository<PushAlarm, Long> {

    Page<PushAlarm> findByOwnerMember(Member member, PageRequest pageRequest);


    List<PushAlarm> findByTitleAndOwnerMemberAndIsConfirmedFalse(String title, Member member);
}
