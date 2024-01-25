package cmc.peerna.domain;

import cmc.peerna.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PeerTest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private Member target;

    @OneToOne
    private Question question;

    @OneToOne
    private Answer answer;

    private String nonMemberUuid;
    public void updateWriter(Member member) {
        this.writer = member;
    }

    public void updateWriterToNull(){
        this.writer = null;
    }
}
