package org.ihiw.management.web.rest;

import org.ihiw.management.domain.*;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.ihiw.management.domain.enumeration.FileType;
import org.ihiw.management.domain.enumeration.ProjectSubscriptionStatus;
import org.ihiw.management.repository.*;
import org.ihiw.management.service.UserService;
import org.ihiw.management.service.dto.UploadDTO;
import org.ihiw.management.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.ihiw.management.security.AuthoritiesConstants.ADMIN;
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
    private final ValidationRepository validationRepository;
    private final FileRepository fileRepository;
    private final IhiwUserRepository ihiwUserRepository;
    private final UserService userService;
    private final ProjectIhiwLabRepository projectIhiwLabRepository;


    public UploadResource(UploadRepository uploadRepository, FileRepository fileRepository, IhiwUserRepository ihiwUserRepository, ValidationRepository validationRepository, UserService userService, ProjectIhiwLabRepository projectIhiwLabRepository) {
        this.uploadRepository = uploadRepository;
        this.fileRepository = fileRepository;
        this.ihiwUserRepository = ihiwUserRepository;
        this.validationRepository = validationRepository;
        this.userService = userService;
        this.projectIhiwLabRepository = projectIhiwLabRepository;
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
        if (upload.getProject() == null) {
            throw new BadRequestAlertException("No project selected for upload", ENTITY_NAME, "missingproject");
        }
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();

        boolean projectMember = false;
        for (ProjectIhiwLab pil : projectIhiwLabRepository.findByLab(currentIhiwUser.getLab())){
            if (pil.getStatus().equals(ProjectSubscriptionStatus.SUBSCRIBED)){
                projectMember = true;
            }
        }

        if (!projectMember) {
            throw new BadRequestAlertException("You are not subscribed to the project", ENTITY_NAME, "missingsubscription");
        }

        String fileName = currentIhiwUser.getId() + "_" + System.currentTimeMillis() + "_" + upload.getType() + "_" + file.getOriginalFilename();
        upload.setFileName(fileName);
        upload.setCreatedBy(currentIhiwUser);
        upload.setCreatedAt(ZonedDateTime.now());
        upload.setModifiedAt(ZonedDateTime.now());

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
     * @param validation the validation object to append, containing the upload object with its filename (mandatory), a "validator", a "valid" status, and "validationFeedback" with the reasons the file is invalid
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated upload,
     * or with status {@code 400 (Bad Request)} if the user is not "validation" with admin permissions.
     * or with status {@code 500 (Internal Server Error)} if the upload couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/uploads/setvalidation")
    @PreAuthorize("hasRole(\"" + VALIDATION + "\")")
    public ResponseEntity<Validation> setUploadValidation(@RequestBody Validation validation) throws URISyntaxException {
        log.debug("REST request to set validation feedback for Upload : {}", validation.getUpload());

        List<Upload> allUploads = uploadRepository.findByFileName(validation.getUpload().getFileName());
        if (allUploads.isEmpty() || allUploads.size() > 1){
        	return ResponseEntity.notFound().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, validation.getUpload().getFileName())).build();
        }

        Upload currentUpload = allUploads.get(0);

        //if there is an existing validation
        Validation updated = null;
        if (!currentUpload.getValidations().isEmpty()){
            for (Validation v : currentUpload.getValidations()){
                if (validation.getValidator().equalsIgnoreCase(v.getValidator())){
                    v.setValid(validation.getValid());
                    v.setValidationFeedback(validation.getValidationFeedback());
                    updated = validationRepository.save(v);
                }
            }
        }
        //if there is no validation yet, create a new one
        if (updated == null){
            Validation v = new Validation();
            v.setUpload(currentUpload);
            v.setValid(validation.getValid());
            v.setValidator(validation.getValidator());
            v.setValidationFeedback(validation.getValidationFeedback());
            updated = validationRepository.save(v);
            currentUpload.getValidations().add(updated);
        }
        uploadRepository.save(currentUpload);

        log.debug("Validation saved:" + updated.getValidator());
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, updated.getValidator()))
            .body(updated);
    }


    /**
     * {@code GET  /uploads} : get all the uploads.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uploads in body.
     */
    @GetMapping("/uploads")
    public ResponseEntity<List<UploadDTO>> getAllUploads(@RequestParam(value = "page" , required = false) Integer offset,
                                                         @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sort", required = false) String sort) {
        log.debug("REST request to get all Uploads");
        Optional<User> currentUser = userService.getUserWithAuthorities();
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();

        Page<UploadDTO> page;
        Pageable pageable;
        
        if(offset != null && size != null) {
            Sort sorting  = Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
            pageable = PageRequest.of(offset, size, sorting);
        } else {
            pageable = Pageable.unpaged();
        }
        
        if (currentUser.get().getAuthorities().contains(new Authority(ADMIN))
        		|| currentUser.get().getAuthorities().contains(new Authority(VALIDATION))) {
            page = userService.getAllUploads(pageable);
        } else {
        	IhiwLab currentLab = currentIhiwUser.getLab();
        	log.debug("Current Lab:" + currentLab.toString());
            List<IhiwUser> colleages = ihiwUserRepository.findByLab(currentLab);
        	log.debug("Colleagues Found:" + colleages.toString());
            page = userService.getAllUploadsByUserId(pageable,colleages);
        }

        for (UploadDTO upload : page) {
            upload.setRawDownload(fileRepository.rawUrl(upload.getFileName()));
            if (upload.getType().equals(FileType.HAML)){
                upload.setConvertedDownload(fileRepository.rawUrl(upload.getFileName() + ".haml"));
            }
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
     * {@code PUT  /uploads/copyupload} : copy the upload, and make a new upload object with a specified Filetype extension.
     *
     * @param oldfileName the name of the previous upload
     * @param newType the filetype of the new file, to assign the new extension.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body of the new upload, or with status {@code 404 (Not Found)}.
     */
    @PutMapping("/uploads/copyupload")
    @PreAuthorize("hasRole(\"" + VALIDATION + "\")")
    public ResponseEntity<Upload> copyUpload(@RequestParam String oldfileName, @RequestParam FileType newType) {

        log.debug("REST request to make an entry for Upload : {}", oldfileName);

        List<Upload> allUploads = uploadRepository.findByFileName(oldfileName);
        Upload result = null;

        if (allUploads.isEmpty()) {
            return ResponseEntity.notFound().headers(HeaderUtil.createAlert(applicationName,  ENTITY_NAME, oldfileName)).build();
        }

        //two entries of this file exist, something is going wrong
        if (allUploads.size() > 1) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oldfileName)).build();
        }

        //fetch the csv upload, like in setUploadValidation.
        Upload oldUpload = allUploads.get(0);

        //one entry exists, everything seems ok
        String newName = oldUpload.getFileName()+ "." + newType.toString().toLowerCase();
        IhiwUser currentUser = oldUpload.getCreatedBy();

        Upload currentUpload = new Upload();
        currentUpload.setFileName(newName);
        currentUpload.setCreatedBy(currentUser);
        currentUpload.setCreatedAt(ZonedDateTime.now());
        currentUpload.setModifiedAt(ZonedDateTime.now());
        currentUpload.setEnabled(oldUpload.getEnabled());
        currentUpload.setType(newType);
        currentUpload.setProject(oldUpload.getProject());

        result = uploadRepository.save(currentUpload);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currentUpload.getFileName()))
            .body(result);
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
