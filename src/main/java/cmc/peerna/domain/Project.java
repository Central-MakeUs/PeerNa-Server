package cmc.peerna.domain;

import cmc.peerna.domain.common.BaseEntity;
import cmc.peerna.domain.mapping.ProjectMember;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private Member creator;
    private Date startDate;
    private Date endDate;
    private String introduce;
    private String openChattingLink;
    private String notionLink;
    private String githubLink;

    @OneToMany(mappedBy = "project")
    List<ProjectMember> projectMemberList;

}
