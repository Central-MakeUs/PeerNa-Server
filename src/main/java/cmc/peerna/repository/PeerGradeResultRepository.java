package cmc.peerna.repository;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.PeerGradeResult;
import cmc.peerna.domain.enums.PeerGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PeerGradeResultRepository extends JpaRepository<PeerGradeResult, Long> {

    PeerGradeResult findByNonMemberUuid(String nonMemberUuid);

    Long countByTargetAndPeerGrade(Member target, PeerGrade peerGrade);

    List<PeerGradeResult> findAllByTarget(Member target);
    List<PeerGradeResult> findAllByWriter(Member writer);

    Long countByTarget(Member target);

    void deleteAllByTarget(Member target);

}
