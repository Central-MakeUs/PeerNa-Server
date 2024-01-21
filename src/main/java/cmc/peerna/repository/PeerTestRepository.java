package cmc.peerna.repository;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.PeerTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeerTestRepository extends JpaRepository<PeerTest, Long> {
    List<PeerTest> findALlByTarget(Member target);
    List<PeerTest> findAllByNonMemberUuid(String nonMemberUuid);
}
