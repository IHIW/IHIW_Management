package org.ihiw.management.repository;

import org.ihiw.management.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Project entity.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findOneById(Long id);

    List<Project> findByLabs(ProjectIhiwLab lab);

    List<Project> findByCreatedBy(IhiwUser currentIhiwUser);

    List<Project> findAllByLeaders(IhiwUser leader);

    List<Project> findAllByOrderByComponent();

    Optional<Project> findById(Long id);

    Page<Project> findAllById(Pageable pageable, Long id);

    @Query("select project from Project project")
    Page<Project> findAll(Pageable pageable);


    @Query("select project from Project project WHERE project.id in (:ids)")
    Page<Project> findByProjectInIds(Pageable pageable, @Param("ids") List<Long> ids);
}
