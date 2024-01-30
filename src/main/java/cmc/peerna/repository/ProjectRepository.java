package cmc.peerna.repository;

import cmc.peerna.domain.Member;
import cmc.peerna.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAllByIdNotNull(PageRequest pageRequest);

    Page<Project> findAllByCreator(Member creator, PageRequest pageRequest);
}
