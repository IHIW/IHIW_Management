package org.ihiw.management.web.rest;

import org.ihiw.management.domain.IhiwUser;
import org.ihiw.management.repository.IhiwUserRepository;
import org.ihiw.management.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.ihiw.management.domain.IhiwUser}.
 */
@RestController
@RequestMapping("/api")
public class IhiwUserResource {

    private final Logger log = LoggerFactory.getLogger(IhiwUserResource.class);

    private static final String ENTITY_NAME = "ihiwUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IhiwUserRepository ihiwUserRepository;

    public IhiwUserResource(IhiwUserRepository ihiwUserRepository) {
        this.ihiwUserRepository = ihiwUserRepository;
    }

    /**
     * {@code POST  /ihiw-users} : Create a new ihiwUser.
     *
     * @param ihiwUser the ihiwUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ihiwUser, or with status {@code 400 (Bad Request)} if the ihiwUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ihiw-users")
    public ResponseEntity<IhiwUser> createIhiwUser(@RequestBody IhiwUser ihiwUser) throws URISyntaxException {
        log.debug("REST request to save IhiwUser : {}", ihiwUser);
        if (ihiwUser.getId() != null) {
            throw new BadRequestAlertException("A new ihiwUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IhiwUser result = ihiwUserRepository.save(ihiwUser);
        return ResponseEntity.created(new URI("/api/ihiw-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ihiw-users} : Updates an existing ihiwUser.
     *
     * @param ihiwUser the ihiwUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ihiwUser,
     * or with status {@code 400 (Bad Request)} if the ihiwUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ihiwUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ihiw-users")
    public ResponseEntity<IhiwUser> updateIhiwUser(@RequestBody IhiwUser ihiwUser) throws URISyntaxException {
        log.debug("REST request to update IhiwUser : {}", ihiwUser);
        if (ihiwUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IhiwUser result = ihiwUserRepository.save(ihiwUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ihiwUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ihiw-users} : get all the ihiwUsers.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ihiwUsers in body.
     */
    @GetMapping("/ihiw-users")
    public List<IhiwUser> getAllIhiwUsers() {
        log.debug("REST request to get all IhiwUsers");
        return ihiwUserRepository.findAll();
    }

    /**
     * {@code GET  /ihiw-users/:id} : get the "id" ihiwUser.
     *
     * @param id the id of the ihiwUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ihiwUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ihiw-users/{id}")
    public ResponseEntity<IhiwUser> getIhiwUser(@PathVariable Long id) {
        log.debug("REST request to get IhiwUser : {}", id);
        Optional<IhiwUser> ihiwUser = ihiwUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ihiwUser);
    }

    /**
     * {@code DELETE  /ihiw-users/:id} : delete the "id" ihiwUser.
     *
     * @param id the id of the ihiwUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ihiw-users/{id}")
    public ResponseEntity<Void> deleteIhiwUser(@PathVariable Long id) {
        log.debug("REST request to delete IhiwUser : {}", id);
        ihiwUserRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
