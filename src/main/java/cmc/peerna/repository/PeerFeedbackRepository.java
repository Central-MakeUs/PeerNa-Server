package cmc.peerna.repository;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.PeerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PeerFeedbackRepository extends JpaRepository<PeerFeedback, Long> {

    PeerFeedback findByNonMemberUuid(String nonMemberUuid);

    List<PeerFeedback> findTop3ByTargetOrderByCreatedAtDesc(Member target);
}
