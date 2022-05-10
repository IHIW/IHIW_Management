package org.ihiw.management.service.mapper;

import org.ihiw.management.domain.Project;
import org.ihiw.management.service.dto.ProjectDTO;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProjectMapper {
    public List<ProjectDTO> ProjectsToProjectDTOs(List<Project> projects) {
        return projects.stream()
            .filter(Objects::nonNull)
            .map(this::projectToProjectDTO)
            .collect(Collectors.toList());
    }



    public ProjectDTO projectToProjectDTO(Project project) {
        return new ProjectDTO(project);
    }



    public Project ProjectDTOToProject(ProjectDTO projectDTO) {
        if (projectDTO == null) {
            return null;
        } else {
            Project project = new Project();
            project.setId(projectDTO.getId());
            project.setName(projectDTO.getName());
            project.setComponent(projectDTO.getComponent());
            project.setDescription(projectDTO.getDescription());
            project.setActivated(projectDTO.getActivated());
            project.setCreatedAt(projectDTO.getCreatedAt());
            project.setCreatedBy(projectDTO.getCreatedBy());
            project.setModifiedAt(projectDTO.getModifiedAt());
            project.setModifiedBy(projectDTO.getModifiedBy());
            project.setLabs(projectDTO.getLabs());
            project.setLeaders(projectDTO.getLeaders());

            return project;
        }
    }

}


