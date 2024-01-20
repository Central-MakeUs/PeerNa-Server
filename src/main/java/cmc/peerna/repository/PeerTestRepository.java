package cmc.peerna.repository;

import cmc.peerna.domain.PeerTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PeerTestRepository extends JpaRepository<PeerTest, Long> {
    List<PeerTest> findAllByNonMemberUuid(String nonMemberUuid);
}
