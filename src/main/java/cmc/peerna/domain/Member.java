package cmc.peerna.domain;

import cmc.peerna.domain.common.BaseEntity;
import cmc.peerna.domain.enums.Job;
import cmc.peerna.domain.enums.Part;
import cmc.peerna.domain.enums.SocialType;
import cmc.peerna.domain.mapping.ProjectMember;
import jakarta.persistence.*;
import lombok.*;

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

    private String image; // 변경 가능

    private String oneliner;

    @Enumerated(EnumType.STRING)
    private Job job;

    @Enumerated(EnumType.STRING)
    private Part part;

    private String socialId;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @OneToMany(mappedBy = "member")
    private List<ProjectMember> projectList;

    @OneToMany(mappedBy = "creator")
    private List<Project> myProjectList;

}
