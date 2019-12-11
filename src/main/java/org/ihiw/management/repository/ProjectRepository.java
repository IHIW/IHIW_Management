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
//
//    @Query(value = "select distinct project from Project project left join fetch project.labs left join fetch project.leaders",
//        countQuery = "select count(distinct project) from Project project")
//    Page<Project> findAllWithEagerRelationships(Pageable pageable);
//
//    @Query("select distinct project from Project project left join fetch project.labs left join fetch project.leaders")
//    List<Project> findAllWithEagerRelationships();
//
//    @Query("select distinct project from Project project left join fetch project.leaders left join fetch project.labs l  where l.id = :id")
//    List<Project> findAllWithEagerRelationshipsByLab(@Param("id") Long labId);
//
//    @Query("select project from Project project left join fetch project.labs left join fetch project.leaders where project.id =:id")
//    Optional<Project> findOneWithEagerRelationships(@Param("id") Long id);
//
//    @Query("select project from Project project left join fetch project.labs left join fetch project.leaders where project.createdBy =:ihiwUser")
//    List<Project> findByCreatedBy(@Param("ihiwUser") IhiwUser ihiwUser);

    Optional<Project> findOneById(Long id);

    List<Project> findByLabs(ProjectIhiwLab lab);

    List<Project> findByCreatedBy(IhiwUser currentIhiwUser);
}
