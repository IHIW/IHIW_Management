package org.ihiw.management.web.rest;

import org.ihiw.management.domain.*;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.ihiw.management.domain.enumeration.FileType;
import org.ihiw.management.domain.enumeration.ProjectSubscriptionStatus;
import org.ihiw.management.repository.*;
import org.ihiw.management.service.UploadService;
import org.ihiw.management.service.UserService;
import org.ihiw.management.service.dto.UploadDTO;
import org.ihiw.management.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.ihiw.management.security.AuthoritiesConstants.ADMIN;
import static org.ihiw.management.security.AuthoritiesConstants.VALIDATION;
import static org.ihiw.management.security.AuthoritiesConstants.PROJECT_LEADER;

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
    private final UploadService uploadService;
    private final ProjectRepository projectRepository;
    private final ProjectIhiwLabRepository projectIhiwLabRepository;


    public UploadResource(UploadRepository uploadRepository, UploadService uploadService, FileRepository fileRepository, IhiwUserRepository ihiwUserRepository, ValidationRepository validationRepository, UserService userService, ProjectRepository projectRepository, ProjectIhiwLabRepository projectIhiwLabRepository) {
        this.uploadRepository = uploadRepository;
        this.fileRepository = fileRepository;
        this.ihiwUserRepository = ihiwUserRepository;
        this.validationRepository = validationRepository;
        this.uploadService = uploadService;
        this.userService = userService;
        this.projectRepository = projectRepository;
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
    public ResponseEntity<Upload> createUpload(@RequestPart Upload upload, @RequestPart List<MultipartFile> files) throws URISyntaxException {
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

        Upload result = null;
        for (MultipartFile file : files){
            Upload currentUpload = new Upload();
            currentUpload.setEnabled(upload.getEnabled());
            currentUpload.setProject(upload.getProject());
            currentUpload.setType(upload.getType());

            currentUpload.setFileName(getFileName(currentIhiwUser, upload, file));
            currentUpload.setCreatedBy(currentIhiwUser);
            currentUpload.setCreatedAt(ZonedDateTime.now());
            currentUpload.setModifiedAt(ZonedDateTime.now());

            result = uploadRepository.save(currentUpload);

            try {
                fileRepository.storeFile(result.getFileName(), file.getBytes());
            } catch (IOException e) {
                log.error("File could not be uploaded: " + upload.getFileName());
            }
        }

        return ResponseEntity.created(new URI("/api/uploads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    private String getFileName(IhiwUser currentIhiwUser, Upload upload, MultipartFile file) {
        return currentIhiwUser.getId() + "_" + System.currentTimeMillis() + "_" + upload.getType() + "_" + file.getOriginalFilename();
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
    public ResponseEntity<UploadDTO> updateUpload(@RequestPart Upload upload, @RequestPart(required = false) MultipartFile file) throws URISyntaxException {
        log.debug("REST request to update Upload : {}", upload);
        if (upload.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Optional<Upload> dbUpload = uploadRepository.findById(upload.getId());

        Optional<User> currentUser = userService.getUserWithAuthorities();
        IhiwUser currentIhiwUser = ihiwUserRepository.findByUserIsCurrentUser();

        if (currentUser.get().getAuthorities().contains(new Authority(ADMIN)) ||
            dbUpload.get().getCreatedBy().getLab().equals(currentIhiwUser.getLab())) {


            if (file != null){
                try {
                    upload.setFileName(getFileName(currentIhiwUser, upload, file));
                    fileRepository.deleteFile(dbUpload.get().getFileName());
                    fileRepository.storeFile(upload.getFileName(), file.getBytes());
                    upload.setValidations(new HashSet<>());
                } catch (IOException e) {
                    log.error("File could not be uploaded: " + upload.getFileName());
                }
            } else {
                if (dbUpload.get().getType() != upload.getType()){
                    //update file type in file name
                    String[] components = upload.getFileName().split("_");
                    if (components.length > 2) {
                        components[2] = upload.getType().name();
                    }
                    String concatFileName = "";
                    for (String k : components){
                        concatFileName += k + "_";
                    }
                    concatFileName = concatFileName.substring(0, concatFileName.length() - 1);
                    upload.setFileName(concatFileName);
                    upload.setValidations(new HashSet<>());
                    upload.setValidations(dbUpload.get().getValidations());
                    uploadRepository.save(upload);
                    fileRepository.renameFile(dbUpload.get().getFileName(), concatFileName);
                }
            }
            UploadDTO uploadDTO = new UploadDTO(upload);
            //Upload result = uploadRepository.save(upload)   ;         //uploadRepository.save(uploadDTO);     //save(upload);
            Optional<UploadDTO> updatedUpload = uploadService.updateUpload(uploadDTO);
            /*return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, upload.getId().toString()))
                .body(result);*/
            return ResponseUtil.wrapOrNotFound(updatedUpload,
                HeaderUtil.createAlert(applicationName, "uploadManagement.updated", uploadDTO.getFileName()));
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
            page = uploadService.getParentlessUploads(pageable);
        } else {
        	IhiwLab currentLab = currentIhiwUser.getLab();
            List<IhiwUser> colleagues = ihiwUserRepository.findByLab(currentLab);
            
        	if (currentUser.get().getAuthorities().contains(new Authority(PROJECT_LEADER))) {
        	    List<Project> projects = projectRepository.findAllByLeaders(currentIhiwUser);
                page = uploadService.getParentlessUploadsByUsersAndProjects(pageable, colleagues, projects);
            }
        	else {
        		page = uploadService.getParentlessUploadsByUserId(pageable, colleagues);
        	}
        }
        
        // Iterate children to create a new "Page" including the Parents with their Children  
        List<UploadDTO> allChildren = new ArrayList<UploadDTO>();
        for (UploadDTO upload : page) {
        	List<Upload> childUploads = uploadService.getAllUploadsByParentId(upload.getId());
            for (Upload childUpload : childUploads) {
            	allChildren.add(new UploadDTO(childUpload));
            }        	
        }
        
        // The Content page contains combined Parent and Children uploads.
        allChildren.addAll(page.getContent());
        Page<UploadDTO> pageWithChildren = new PageImpl<UploadDTO>(allChildren, Pageable.unpaged(), allChildren.size());

        for (UploadDTO upload : pageWithChildren) {
            upload.setRawDownload(fileRepository.rawUrl(upload.getFileName()));
        }

        // HTTP Headers use indexing from original page, which indexed only the parent uploads.
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(pageWithChildren.getContent(), headers, HttpStatus.OK);
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
            return ResponseUtil.wrapOrNotFound(upload);
        }
        return ResponseEntity.notFound().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
    
    
    
    /**
     * {@code GET  /uploads/children/:id} : get the children of the parent "id" upload.
     *
     * @param id the id of the parent upload, we find the children of.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body of the child uploads.
     */
    @GetMapping("/uploads/children/{id}")
    @PreAuthorize("hasRole(\"" + VALIDATION + "\")")
    public ResponseEntity<List<Upload>> getChildren(@PathVariable Long id) {
        log.debug("REST request to get Child Uploads by Parent ID : {}", id);

        List<Upload> childUploads = uploadRepository.findChildrenById(id);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, childUploads.toString()))
            .body(childUploads);        
    }

    /**
     * {@code PUT  /uploads/copyupload} : copy the upload, and make a new upload object with a specified Filetype extension.
     *
     * @param oldFileName the name of the previous (parent) upload
     * @param newFileName the name of the new (child) upload
     * @param newType the FileType of the new child upload, to assign the new extension.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body of the new upload, or with status {@code 404 (Not Found)}.
     */
    @PutMapping("/uploads/copyupload")
    @PreAuthorize("hasRole(\"" + VALIDATION + "\")")
    public ResponseEntity<Upload> copyUpload(@RequestParam String oldFileName, @RequestParam String newFileName, @RequestParam FileType newType) {

        log.debug("REST request to make an entry for Upload : {}", oldFileName);

        List<Upload> allUploads = uploadRepository.findByFileName(oldFileName);
        Upload result = null;

        if (allUploads.isEmpty()) {
            return ResponseEntity.notFound().headers(HeaderUtil.createAlert(applicationName,  ENTITY_NAME, oldFileName)).build();
        }
        else if (allUploads.size() > 1) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oldFileName)).build();
        }

        Upload parentUpload = allUploads.get(0);
        IhiwUser creator = parentUpload.getCreatedBy();

        Upload currentUpload = new Upload();
        currentUpload.setFileName(newFileName);
        currentUpload.setCreatedBy(creator);
        currentUpload.setCreatedAt(ZonedDateTime.now());
        currentUpload.setModifiedAt(ZonedDateTime.now());
        currentUpload.setEnabled(parentUpload.getEnabled());
        currentUpload.setType(newType);
        currentUpload.setProject(parentUpload.getProject());
        currentUpload.setParentUpload(parentUpload);

        log.debug("Saving new child upload : {}", newFileName);

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
        	currentUser.get().getAuthorities().contains(new Authority(VALIDATION)) ||
            upload.get().getCreatedBy().getLab().equals(currentIhiwUser.getLab())) {

        	// Delete each child of this parent upload.
        	for (Upload childUpload : uploadRepository.findChildrenById(id)) {
        		log.debug("Upload " + id.toString() + " has a child upload which will be deleted: " + childUpload.toString());
        		fileRepository.deleteFile(childUpload.getFileName());
        		uploadRepository.deleteById(childUpload.getId());
        	}

        	// Then delete this parent upload.
            fileRepository.deleteFile(upload.get().getFileName());
            uploadRepository.deleteById(id);

            return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
        }
        return ResponseEntity.notFound().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code GET  /uploads/getbyfilename/:fileName} : get the "fileName" upload.
     *
     * @param fileName the fileName of the upload to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the upload, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/uploads/getbyfilename/{fileName}")
    @PreAuthorize("hasRole(\"" + VALIDATION + "\")")
    public ResponseEntity<Upload> getUploadByFilename(@PathVariable String fileName) {
        log.debug("REST request to get Upload by fileName : {}", fileName);
        if (fileName == null) {
            throw new BadRequestAlertException("Invalid fileName", ENTITY_NAME, "filenamenull");
        }

        List<Upload> uploads = uploadRepository.findByFileName(fileName);

        if(uploads.size()==0) {
        	return ResponseEntity.notFound().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, fileName.toString())).build();
        }
        else if(uploads.size() > 1) {
        	return ResponseEntity.notFound().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, "Multiple Uploads Found:" + fileName.toString())).build();
        }
        else {
        	Optional<Upload> upload = Optional.of(uploads.get(0));
        	return ResponseUtil.wrapOrNotFound(upload);
        }

    }
    
    
    /**
     * {@code GET  /uploads/getbyproject/:projectId} : get the "projectId" uploads.
     *
     * @param projectId the projectId assigned to the uploads
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the upload, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/uploads/getbyproject/{projectId}")
    @PreAuthorize("hasRole(\"" + VALIDATION + "\")")
    public ResponseEntity<List<Upload>> getUploadsByProject(@PathVariable Long projectId) {
        log.debug("REST request to get Upload by project ID : {}", projectId);
        if (projectId == null) {
            throw new BadRequestAlertException("Invalid projectId", ENTITY_NAME, "projectidnull");
        }

        List<Upload> uploads = uploadRepository.findAllByProjectId(projectId);

        if(uploads.size()==0) {
        	return ResponseEntity.notFound().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, projectId.toString())).build();
        }
        else {
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, projectId.toString()))
                .body(uploads);    
        }

    }
    
}
