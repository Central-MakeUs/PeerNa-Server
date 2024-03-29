package cmc.peerna.repository;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.PeerFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PeerFeedbackRepository extends JpaRepository<PeerFeedback, Long> {

    Optional<PeerFeedback> findByNonMemberUuid(String nonMemberUuid);

    List<PeerFeedback> findTop3ByTargetOrderByCreatedAtDesc(Member target);

    Page<PeerFeedback> findAllByTarget(Member target, PageRequest pageRequest);

    List<PeerFeedback> findAllByWriter(Member writer);

    void deleteAllByTarget(Member target);

    boolean existsByTarget(Member target);
}
