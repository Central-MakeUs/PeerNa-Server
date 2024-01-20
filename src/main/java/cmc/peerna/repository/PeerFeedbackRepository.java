package cmc.peerna.repository;

import cmc.peerna.domain.PeerFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeerFeedbackRepository extends JpaRepository<PeerFeedback, Long> {
}
