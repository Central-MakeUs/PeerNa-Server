package cmc.peerna.repository;

import cmc.peerna.domain.GuestPeerTestIp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestPeerTestIpRepository extends JpaRepository<GuestPeerTestIp, Long> {
    boolean existsByUuidAndIp(String uuid, String ip);
}
