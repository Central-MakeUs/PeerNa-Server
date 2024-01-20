package cmc.peerna.domain;

import cmc.peerna.domain.common.BaseEntity;
import cmc.peerna.domain.enums.PeerGrade;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PeerGradeResult extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Member writer;
    @OneToOne
    private Member target;
    private PeerGrade peerGrade;

    private String nonMemberUuid;
    public void updateWriter(Member member) {
        this.writer = member;
    }
}
