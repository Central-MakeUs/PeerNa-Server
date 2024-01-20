package cmc.peerna.repository;

import cmc.peerna.domain.PeerGradeResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PeerGradeResultRepository extends JpaRepository<PeerGradeResult, Long> {

    PeerGradeResult findByNonMemberUuid(String nonMemberUuid);

}
