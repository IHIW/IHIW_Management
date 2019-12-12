package org.ihiw.management.web.rest;

import org.ihiw.management.domain.*;
import org.ihiw.management.domain.enumeration.ProjectSubscriptionStatus;
import org.ihiw.management.repository.IhiwUserRepository;
import org.ihiw.management.repository.ProjectIhiwLabRepository;
import org.ihiw.management.repository.ProjectRepository;
import org.ihiw.management.repository.UserRepository;
import org.ihiw.management.security.AuthoritiesConstants;
import org.ihiw.management.security.SecurityUtils;
import org.ihiw.management.service.MailService;
import org.ihiw.management.service.UserService;
import org.ihiw.management.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    private final ProjectIhiwLabRepository projectIhiwLabRepository;
    private final IhiwUserRepository ihiwUserRepository;
    private final UserService userService;
    private final MailService mailService;

    public ProjectResource(ProjectRepository projectRepository, ProjectIhiwLabRepository projectIhiwLabRepository, IhiwUserRepository ihiwUserRepository, UserService userService, MailService mailService) {
        this.projectRepository = projectRepository;
        this.ihiwUserRepository = ihiwUserRepository;
        this.projectIhiwLabRepository = projectIhiwLabRepository;
        this.userService = userService;
        this.mailService = mailService;
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

    @PostMapping("/projects/{projectId}/subscribe")
    //FIXME: why the heck is preauthorize not working for the PI?
//    @PreAuthorize("hasAnyRole('" + AuthoritiesConstants.PI + "', '" + AuthoritiesConstants.PROJECT_LEADER + "')")
    public ResponseEntity<ProjectIhiwLab> subscribeProject(@PathVariable long projectId) throws URISyntaxException {
        log.debug("REST request to subscribe to Project : {}", projectId);
        Optional<User> currentUser = userService.getUserWithAuthorities();
        if (currentUser.get().getAuthorities().contains(new Authority(AuthoritiesConstants.PI)) ||
            currentUser.get().getAuthorities().contains(new Authority(AuthoritiesConstants.PROJECT_LEADER))){
            IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();

            Optional<Project> projectFromDB = projectRepository.findOneById(projectId);
            ProjectIhiwLab projectIhiwLab = projectIhiwLabRepository.findByLabAndProject(currentIhiwUser.getLab(), projectFromDB.get());
            // if there is no relation yet
            boolean isNew = false;
            if (projectIhiwLab == null){
                projectIhiwLab = new ProjectIhiwLab();
                projectIhiwLab.setLab(currentIhiwUser.getLab());
                projectIhiwLab.setProject(projectFromDB.get());
                isNew = true;
            }
            projectIhiwLab.setStatus(ProjectSubscriptionStatus.REQUESTED);
            projectIhiwLab = projectIhiwLabRepository.save(projectIhiwLab);
            if (isNew){
                projectFromDB.get().getLabs().add(projectIhiwLab);
                projectRepository.save(projectFromDB.get());
            }

            // send all project leaders a notification
            for (IhiwUser leader : projectFromDB.get().getLeaders()){
                mailService.sendProjectLeaderSubscriptionNotificationEmail(leader.getUser());
            }

            return ResponseEntity.created(new URI("/api/projects/" + projectId + "/subscribe"))
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectId + ""))
                .body(projectIhiwLab);
        }
        return ResponseEntity.notFound().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, projectId + "")).build();
    }

    @PostMapping("/projects/{projectId}/unsubscribe")
    public ResponseEntity<ProjectIhiwLab> unsubscribeProject(@PathVariable long projectId) throws URISyntaxException {
        log.debug("REST request to subscribe to Project : {}", projectId);
        Optional<User> currentUser = userService.getUserWithAuthorities();
        if (currentUser.get().getAuthorities().contains(new Authority(AuthoritiesConstants.PI))){
            IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();

            Optional<Project> projectFromDB = projectRepository.findOneById(projectId);
            ProjectIhiwLab projectIhiwLab = projectIhiwLabRepository.findByLabAndProject(currentIhiwUser.getLab(), projectFromDB.get());
            if (projectIhiwLab == null){
                projectIhiwLab = new ProjectIhiwLab();
                projectIhiwLab.setLab(currentIhiwUser.getLab());
                projectIhiwLab.setProject(projectFromDB.get());
            }
            projectIhiwLab.setStatus(ProjectSubscriptionStatus.UNSUBSCRIBED);
            projectIhiwLab = projectIhiwLabRepository.save(projectIhiwLab);

            return ResponseEntity.created(new URI("/api/projects/" + projectId + "/subscribe"))
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectId + ""))
                .body(projectIhiwLab);
        }
        return ResponseEntity.notFound().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, projectId + "")).build();
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
        Optional<Project> projectFromDB = projectRepository.findOneById(project.getId());
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();
        Optional<User> currentUser = userService.getUserWithAuthorities();
        if (currentUser.get().getAuthorities().contains(new Authority(ADMIN)) ||
            projectFromDB.get().getCreatedBy().equals(currentIhiwUser)) {
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
                currentUser.get().getAuthorities().contains(new Authority(PI))){
            List<Project> result = projectRepository.findAll();
            return result;
        }
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();
        List<ProjectIhiwLab> projectIhiwLabs = projectIhiwLabRepository.findByLab(currentIhiwUser.getLab());
        List<Project> projects = new ArrayList<>();
        for (ProjectIhiwLab pil : projectIhiwLabs) {
            projects.add(pil.getProject());
        }
        return projects;
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
        Optional<Project> project = projectRepository.findById(id);
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();
        Optional<User> currentUser = userService.getUserWithAuthorities();
        if (currentUser.get().getAuthorities().contains(new Authority(ADMIN)) ||
            currentUser.get().getAuthorities().contains(new Authority(WORKSHOP_CHAIR)) ||
            currentUser.get().getAuthorities().contains(new Authority(PI))){
            return ResponseUtil.wrapOrNotFound(project);
        }
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
        Optional<Project> project = projectRepository.findById(id);
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
