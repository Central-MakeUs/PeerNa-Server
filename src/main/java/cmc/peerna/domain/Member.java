package cmc.peerna.domain;

import cmc.peerna.domain.common.BaseEntity;
import cmc.peerna.domain.enums.*;
import cmc.peerna.domain.mapping.ProjectMember;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TestType testType;

    @Enumerated(EnumType.STRING)
    private PeerGrade selfPeerGrade;

    private String oneliner;

    @Enumerated(EnumType.STRING)
    private Job job;

    @Enumerated(EnumType.STRING)
    private Part part;

    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    @ColumnDefault("'USER'")
    private UserRole userRole;
    @OneToMany(mappedBy = "member")
    private List<ProjectMember> projectList;

    @OneToMany(mappedBy = "creator")
    private List<Project> myProjectList;

    public void setBasicInfo(MemberRequestDto.basicInfoDTO request) {
        this.name = request.getName();
        this.job = request.getJob();
        this.part = request.getPart();
        this.selfPeerGrade = request.getSelfPeerGrade();
        this.oneliner = request.getOneLiner();
    }
}
