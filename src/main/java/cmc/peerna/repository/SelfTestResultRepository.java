package cmc.peerna.repository;

import cmc.peerna.domain.SelfTestResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SelfTestResultRepository extends JpaRepository<SelfTestResult, Long> {
}
