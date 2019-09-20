package org.ihiw.management.web.rest;

import org.ihiw.management.domain.*;
import org.ihiw.management.repository.IhiwUserRepository;
import org.ihiw.management.repository.ProjectRepository;
import org.ihiw.management.repository.UserRepository;
import org.ihiw.management.security.AuthoritiesConstants;
import org.ihiw.management.security.SecurityUtils;
import org.ihiw.management.service.UserService;
import org.ihiw.management.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.ihiw.management.security.AuthoritiesConstants.*;
/**
 * REST controller for managing {@link org.ihiw.management.domain.Project}.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    private static final String ENTITY_NAME = "project";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProjectRepository projectRepository;
    private final IhiwUserRepository ihiwUserRepository;
    private final UserService userService;

    public ProjectResource(ProjectRepository projectRepository, IhiwUserRepository ihiwUserRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.ihiwUserRepository = ihiwUserRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /projects} : Create a new project.
     *
     * @param project the project to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new project, or with status {@code 400 (Bad Request)} if the project has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/projects")
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.ADMIN + "', '" + AuthoritiesConstants.PROJECT_LEADER + "')")
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to save Project : {}", project);
        if (project.getId() != null) {
            throw new BadRequestAlertException("A new project cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (project.getCreatedBy() == null){
            project.setCreatedBy(ihiwUserRepository.findByUserIsCurrentUser());
            if (project.getCreatedAt() == null){
                project.setModifiedBy(project.getCreatedBy());
            }
        }

        project.setCreatedAt(ZonedDateTime.now());
        project.setModifiedAt(ZonedDateTime.now());

        Project result = projectRepository.save(project);
        return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /projects} : Updates an existing project.
     *
     * @param project the project to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated project,
     * or with status {@code 400 (Bad Request)} if the project is not valid,
     * or with status {@code 500 (Internal Server Error)} if the project couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/projects")
    public ResponseEntity<Project> updateProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to update Project : {}", project);
        if (project.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Project projectFromDB = projectRepository.getOne(project.getId());
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();
        Optional<User> currentUser = userService.getUserWithAuthorities();
        if (currentUser.get().getAuthorities().contains(new Authority(ADMIN)) ||
            projectFromDB.getCreatedBy().equals(currentIhiwUser)) {
            Project result = projectRepository.save(project);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, project.getId().toString()))
                .body(result);
        }
        return ResponseEntity.notFound().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, project.getId().toString())).build();
    }

    /**
     * {@code GET  /projects} : get all the projects.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of projects in body.
     */
    @GetMapping("/projects")
    public List<Project> getAllProjects(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Projects");
        Optional<User> currentUser = userService.getUserWithAuthorities();
        if (currentUser.get().getAuthorities().contains(new Authority(ADMIN)) ||
            currentUser.get().getAuthorities().contains(new Authority(WORKSHOP_CHAIR)) ||
            currentUser.get().getAuthorities().contains(new Authority(PROJECT_LEADER))){
            return projectRepository.findAllWithEagerRelationships();
        }
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();
        return projectRepository.findAllWithEagerRelationshipsByLab(currentIhiwUser.getLab().getId());
    }

    /**
     * {@code GET  /projects/:id} : get the "id" project.
     *
     * @param id the id of the project to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the project, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        Optional<Project> project = projectRepository.findOneWithEagerRelationships(id);
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();
        if (project.get().getLabs().contains(currentIhiwUser.getLab())){
            return ResponseUtil.wrapOrNotFound(project);
        }
        return ResponseEntity.notFound().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code DELETE  /projects/:id} : delete the "id" project.
     *
     * @param id the id of the project to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/projects/{id}")
    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.ADMIN + "', '" + AuthoritiesConstants.PROJECT_LEADER + "')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        Optional<Project> project = projectRepository.findOneWithEagerRelationships(id);
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();
        Optional<User> currentUser = userService.getUserWithAuthorities();
        if (!currentUser.get().getAuthorities().contains(new Authority(ADMIN))){
            if (!project.get().getCreatedBy().equals(currentIhiwUser)){
                return ResponseEntity.badRequest().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
            }
        }
        projectRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
