package cmc.peerna.repository;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.Project;
import cmc.peerna.domain.mapping.ProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    boolean existsByProjectIdAndMemberId(Long projectId, Long memberId);

    @Query("select p.project from ProjectMember p where p.member= :member order by p.createdAt desc ")
    List<Project> qFindProjectByMemberOrderByCreatedAtDesc(@Param("member") Member member);

    @Query("select p.project from ProjectMember p where p.member= :member order by p.project.startDate desc")
    Page<Project> qFindProjectPageByMemberOrderByCreatedAtDesc(@Param("member") Member member, PageRequest pageRequest);

    void deleteAllByMember(Member member);

    Integer countByMember(Member member);
}
