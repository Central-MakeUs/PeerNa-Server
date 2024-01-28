package cmc.peerna.domain;

import cmc.peerna.domain.common.BaseEntity;
import cmc.peerna.domain.enums.*;
import cmc.peerna.domain.mapping.ProjectMember;
import cmc.peerna.web.dto.requestDto.MemberRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;
import java.util.UUID;

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
    private TestType selfTestType;

    @Enumerated(EnumType.STRING)
    private TestType peerTestType;

    @Enumerated(EnumType.STRING)
    private PeerGrade selfPeerGrade;

    private String oneliner;

    @Enumerated(EnumType.STRING)
    private Job job;

    @Enumerated(EnumType.STRING)
    private Part part;

    @ColumnDefault("0")
    private Integer totalScore;

    private String socialId;
    private String uuid;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @ColumnDefault("false")
    private boolean pushAgree;

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
        if(this.uuid == null)
            this.uuid = UUID.randomUUID().toString();
    }

    public void setTestType(TestType selfTestType) {
        this.selfTestType=selfTestType;
    }

    public void updateTotalScore(Integer totalScore){ this.totalScore=totalScore;}
    public void updatePeerTestType(TestType peerTestType){ this.peerTestType = peerTestType;}

    public boolean updatePushAgree(boolean pushAgree){
        this.pushAgree=pushAgree;
        return this.pushAgree;
    }

    public void updateProfile(MemberRequestDto.profileUpdateDto request) {
        this.job = request.getJob();
        this.part = request.getPart();
        this.oneliner = request.getOneLiner();
    }
}
