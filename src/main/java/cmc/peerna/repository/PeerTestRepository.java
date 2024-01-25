package cmc.peerna.repository;

import cmc.peerna.domain.Answer;
import cmc.peerna.domain.Member;
import cmc.peerna.domain.PeerTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeerTestRepository extends JpaRepository<PeerTest, Long> {
    List<PeerTest> findALlByTarget(Member target);
    List<PeerTest> findALlByWriter(Member writer);
    List<PeerTest> findAllByNonMemberUuid(String nonMemberUuid);

    Long countByTargetAndAnswerId(Member target, Long answerId);

    void deleteAllByTarget(Member target);

}
