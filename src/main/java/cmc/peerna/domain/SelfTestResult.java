package cmc.peerna.domain;

import cmc.peerna.domain.common.BaseEntity;
import cmc.peerna.domain.enums.PeerCard;
import cmc.peerna.domain.enums.TestType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SelfTestResult extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Member member;

    @Enumerated(EnumType.STRING)
    private TestType testType;

    @Enumerated(EnumType.STRING)
    private PeerCard group1;

    @Enumerated(EnumType.STRING)
    private PeerCard group2;

    @Enumerated(EnumType.STRING)
    private PeerCard group3;

    @Enumerated(EnumType.STRING)
    private PeerCard group4;

}
