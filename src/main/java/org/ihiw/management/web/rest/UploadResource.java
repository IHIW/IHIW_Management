package org.ihiw.management.web.rest;

import org.ihiw.management.domain.Authority;
import org.ihiw.management.domain.IhiwUser;
import org.ihiw.management.domain.Upload;
import org.ihiw.management.domain.User;
import org.ihiw.management.domain.enumeration.FileType;
import org.ihiw.management.repository.FileRepository;
import org.ihiw.management.repository.IhiwUserRepository;
import org.ihiw.management.repository.UploadRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.ihiw.management.security.AuthoritiesConstants.ADMIN;
import static org.ihiw.management.security.AuthoritiesConstants.PROJECT_LEADER;
import static org.ihiw.management.security.AuthoritiesConstants.VALIDATION;

/**
 * REST controller for managing {@link org.ihiw.management.domain.Upload}.
 */
@RestController
@RequestMapping("/api")
public class UploadResource {

    private final Logger log = LoggerFactory.getLogger(UploadResource.class);

    private static final String ENTITY_NAME = "upload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UploadRepository uploadRepository;
    private final FileRepository fileRepository;
    private final IhiwUserRepository ihiwUserRepository;
    private final UserService userService;

    public UploadResource(UploadRepository uploadRepository, FileRepository fileRepository, IhiwUserRepository ihiwUserRepository, UserService userService) {
        this.uploadRepository = uploadRepository;
        this.fileRepository = fileRepository;
        this.ihiwUserRepository = ihiwUserRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /uploads} : Create a new upload.
     *
     * @param upload the upload to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new upload, or with status {@code 400 (Bad Request)} if the upload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/uploads")
    public ResponseEntity<Upload> createUpload(@RequestPart Upload upload, @RequestPart MultipartFile file) throws URISyntaxException {
        log.debug("REST request to save Upload : {}", upload);
        if (upload.getId() != null) {
            throw new BadRequestAlertException("A new upload cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();

        String fileName = currentIhiwUser.getId() + "_" + System.currentTimeMillis() + "_" + upload.getType() + "_" + file.getOriginalFilename();
        upload.setFileName(fileName);
        upload.setCreatedBy(currentIhiwUser);
        upload.setCreatedAt(ZonedDateTime.now());
        upload.setModifiedAt(ZonedDateTime.now());
        upload.setValid(false);

        Upload result = uploadRepository.save(upload);

        try {
            fileRepository.storeFile(fileName, file.getBytes());
        } catch (IOException e) {
            log.error("File could not be uploaded: " + upload.getFileName());
        }

        return ResponseEntity.created(new URI("/api/uploads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /uploads} : Updates an existing upload.
     *
     * @param upload the upload to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated upload,
     * or with status {@code 400 (Bad Request)} if the upload is not valid,
     * or with status {@code 500 (Internal Server Error)} if the upload couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/uploads")
    public ResponseEntity<Upload> updateUpload(@RequestBody Upload upload, @RequestPart MultipartFile file) throws URISyntaxException {
        log.debug("REST request to update Upload : {}", upload);
        if (upload.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Upload> dbUpload = uploadRepository.findById(upload.getId());

        Optional<User> currentUser = userService.getUserWithAuthorities();
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();

        if (currentUser.get().getAuthorities().contains(new Authority(ADMIN)) ||
            dbUpload.get().getCreatedBy().getLab().equals(currentIhiwUser.getLab())) {

            fileRepository.deleteFile(dbUpload.get().getFileName());
            fileRepository.deleteFile(dbUpload.get().getFileName() + ".haml");

            try {
                fileRepository.storeFile(upload.getFileName(), file.getBytes());
            } catch (IOException e) {
                log.error("File could not be uploaded: " + upload.getFileName());
            }

            Upload result = uploadRepository.save(upload);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, upload.getId().toString()))
                .body(result);
        }
        return ResponseEntity.badRequest().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, upload.getId().toString())).build();
    }


    /**
     * {@code PUT  /uploads/setvalidation} : Set validation status on an existing upload.
     *
     * @param upload the upload to update, containing the filename to be updated, a "valid" status, and "validationFeedback" with the reasons the file is invalid
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated upload,
     * or with status {@code 400 (Bad Request)} if the user is not "validation" with admin permissions.
     * or with status {@code 500 (Internal Server Error)} if the upload couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/uploads/setvalidation")
    @PreAuthorize("hasRole(\"" + VALIDATION + "\")")
    public ResponseEntity<Upload> setUploadValidation(@RequestBody Upload upload) throws URISyntaxException {
        log.debug("REST request to set validation feedback for Upload : {}", upload);

        List<Upload> allUploads = uploadRepository.findAll();

        Upload dbUpload = null;
        for (Upload currentUpload : allUploads) {
        	if(currentUpload.getFileName().equals(upload.getFileName())) {
        		dbUpload=currentUpload;
        	}
        }

        if(dbUpload==null){
        	return ResponseEntity.notFound().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, upload.getFileName().toString())).build();
	    }
        else {
        	dbUpload.setValid(upload.isValid());
        	dbUpload.setValidationFeedback(upload.getValidationFeedback());
        }

        Upload result = uploadRepository.save(dbUpload);
        log.debug("Upload saved:" + result.toString());

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, upload.getFileName().toString()))
            .body(result);
    }


    /**
     * {@code GET  /uploads} : get all the uploads.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uploads in body.
     */
    @GetMapping("/uploads")
    public List<Upload> getAllUploads() {
        log.debug("REST request to get all Uploads");
        Optional<User> currentUser = userService.getUserWithAuthorities();
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();
        List<IhiwUser> colleages = ihiwUserRepository.findByLab(currentIhiwUser.getLab());

        List<Upload> result;
        if (currentUser.get().getAuthorities().contains(new Authority(ADMIN))) {
            result = uploadRepository.findAll();
        } else {
            result = uploadRepository.findByCreatedByIn(colleages);
        }

        for (Upload upload : result) {
            upload.setRawDownload(fileRepository.rawUrl(upload.getFileName()));
            if (upload.getType().equals(FileType.HAML)){
                upload.setConvertedDownload(fileRepository.rawUrl(upload.getFileName() + ".haml"));
            }
        }
        return result;
    }

    /**
     * {@code GET  /uploads/:id} : get the "id" upload.
     *
     * @param id the id of the upload to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the upload, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/uploads/{id}")
    public ResponseEntity<Upload> getUpload(@PathVariable Long id) {
        log.debug("REST request to get Upload : {}", id);
        Optional<Upload> upload = uploadRepository.findById(id);

        Optional<User> currentUser = userService.getUserWithAuthorities();
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();

        if (currentUser.get().getAuthorities().contains(new Authority(ADMIN)) ||
            upload.get().getCreatedBy().getLab().equals(currentIhiwUser.getLab())) {
            upload.get().setRawDownload(fileRepository.rawUrl(upload.get().getFileName()));
            if (upload.get().getType().equals(FileType.HAML)){
                upload.get().setConvertedDownload(fileRepository.rawUrl(upload.get().getFileName() + ".haml"));
            }
            return ResponseUtil.wrapOrNotFound(upload);
        }
        return ResponseEntity.notFound().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code DELETE  /uploads/:id} : delete the "id" upload.
     *
     * @param id the id of the upload to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/uploads/{id}")
    public ResponseEntity<Void> deleteUpload(@PathVariable Long id) {
        log.debug("REST request to delete Upload : {}", id);
        Optional<Upload> upload = uploadRepository.findById(id);

        Optional<User> currentUser = userService.getUserWithAuthorities();
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();

        if (currentUser.get().getAuthorities().contains(new Authority(ADMIN)) ||
            upload.get().getCreatedBy().getLab().equals(currentIhiwUser.getLab())) {
            fileRepository.deleteFile(upload.get().getFileName());
            fileRepository.deleteFile(upload.get().getFileName() + ".haml");
            uploadRepository.deleteById(id);
            return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
        }
        return ResponseEntity.notFound().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
