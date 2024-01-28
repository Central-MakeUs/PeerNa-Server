package cmc.peerna.repository;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.SelfTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SelfTestRepository extends JpaRepository<SelfTest, Long> {

    void deleteAllByWriter(Member writer);

    List<SelfTest> findAllByWriter(Member writer);

    boolean existsByWriter(Member writer);
}
