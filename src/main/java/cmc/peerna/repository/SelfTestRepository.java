package cmc.peerna.repository;

import cmc.peerna.domain.SelfTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelfTestRepository extends JpaRepository<SelfTest, Long> {
}
