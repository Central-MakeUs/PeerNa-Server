package cmc.peerna.repository;

import cmc.peerna.domain.FcmToken;
import cmc.peerna.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByMember(Member member);

    void deleteByMemberAndToken(Member member, String token);

    void deleteAllByMember(Member member);

    boolean existsByMemberAndToken(Member member, String token);
}
