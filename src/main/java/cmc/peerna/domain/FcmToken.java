package cmc.peerna.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Builder
@Entity
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String token;

    public FcmToken(final String token, final Member member) {
        this.token = token;
        this.member = member;
    }

    public void update(final String token) {
        this.token = token;
    }
}
