package cmc.peerna.repository;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.enums.Part;
import cmc.peerna.domain.enums.SocialType;
import cmc.peerna.domain.enums.TestType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<Member> findByUuid(String uuid);

    Page<Member> findAllByPeerTestTypeAndIdNot(TestType peerTestType, Long memberId, PageRequest pageRequest);

    Page<Member> findAllByPartAndIdNot(Part part, Long memberId, PageRequest pageRequest);


}
