package org.ihiw.management.repository;

import org.ihiw.management.domain.IhiwLab;
import org.ihiw.management.domain.Project;
import org.ihiw.management.domain.ProjectIhiwLab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectIhiwLabRepository extends JpaRepository<ProjectIhiwLab, Long> {
    List<ProjectIhiwLab> findByLab(IhiwLab lab);
    ProjectIhiwLab findByLabAndProject(IhiwLab lab, Project project);
}
