package org.ihiw.management.web.rest;

import org.ihiw.management.domain.IhiwLab;
import org.ihiw.management.repository.IhiwLabRepository;
import org.ihiw.management.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link org.ihiw.management.domain.IhiwLab}.
 */
@RestController
@RequestMapping("/api")
public class IhiwLabResource {

    private final Logger log = LoggerFactory.getLogger(IhiwLabResource.class);

    private static final String ENTITY_NAME = "ihiwLab";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IhiwLabRepository ihiwLabRepository;

    public IhiwLabResource(IhiwLabRepository ihiwLabRepository) {
        this.ihiwLabRepository = ihiwLabRepository;
    }

    /**
     * {@code POST  /ihiw-labs} : Create a new ihiwLab.
     *
     * @param ihiwLab the ihiwLab to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ihiwLab, or with status {@code 400 (Bad Request)} if the ihiwLab has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ihiw-labs")
    public ResponseEntity<IhiwLab> createIhiwLab(@Valid @RequestBody IhiwLab ihiwLab) throws URISyntaxException {
        log.debug("REST request to save IhiwLab : {}", ihiwLab);
        if (ihiwLab.getId() != null) {
            throw new BadRequestAlertException("A new ihiwLab cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ihiwLab.setCreatedAt(ZonedDateTime.now());

        IhiwLab result = ihiwLabRepository.save(ihiwLab);
        return ResponseEntity.created(new URI("/api/ihiw-labs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ihiw-labs} : Updates an existing ihiwLab.
     *
     * @param ihiwLab the ihiwLab to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ihiwLab,
     * or with status {@code 400 (Bad Request)} if the ihiwLab is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ihiwLab couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ihiw-labs")
    public ResponseEntity<IhiwLab> updateIhiwLab(@Valid @RequestBody IhiwLab ihiwLab) throws URISyntaxException {
        log.debug("REST request to update IhiwLab : {}", ihiwLab);
        if (ihiwLab.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        IhiwLab result = ihiwLabRepository.save(ihiwLab);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ihiwLab.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ihiw-labs} : get all the ihiwLabs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ihiwLabs in body.
     */
    @GetMapping("/ihiw-labs")
    public List<IhiwLab> getAllIhiwLabs() {
        log.debug("REST request to get all IhiwLabs");
        return ihiwLabRepository.findAll();
    }

    /**
     * {@code GET  /ihiw-labs/:id} : get the "id" ihiwLab.
     *
     * @param id the id of the ihiwLab to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ihiwLab, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ihiw-labs/{id}")
    public ResponseEntity<IhiwLab> getIhiwLab(@PathVariable Long id) {
        log.debug("REST request to get IhiwLab : {}", id);
        Optional<IhiwLab> ihiwLab = ihiwLabRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ihiwLab);
    }

    /**
     * {@code DELETE  /ihiw-labs/:id} : delete the "id" ihiwLab.
     *
     * @param id the id of the ihiwLab to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ihiw-labs/{id}")
    public ResponseEntity<Void> deleteIhiwLab(@PathVariable Long id) {
        log.debug("REST request to delete IhiwLab : {}", id);
        ihiwLabRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
