package org.ihiw.management.web.rest;

import org.ihiw.management.domain.*;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.ihiw.management.domain.enumeration.FileType;
import org.ihiw.management.repository.FileRepository;
import org.ihiw.management.repository.IhiwUserRepository;
import org.ihiw.management.repository.UploadRepository;
import org.ihiw.management.repository.ValidationRepository;
import org.ihiw.management.service.UserService;
import org.ihiw.management.service.dto.UploadDTO;
import org.ihiw.management.web.rest.errors.BadRequestAlertException;
import org.joda.time.DateTime;
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
import java.sql.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
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

    static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    static final String DB_URL = "jdbc:mariadb://localhost:3306";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UploadRepository uploadRepository;
    private final ValidationRepository validationRepository;
    private final FileRepository fileRepository;
    private final IhiwUserRepository ihiwUserRepository;
    private final UserService userService;

    public UploadResource(UploadRepository uploadRepository, FileRepository fileRepository, IhiwUserRepository ihiwUserRepository, ValidationRepository validationRepository, UserService userService) {
        this.uploadRepository = uploadRepository;
        this.fileRepository = fileRepository;
        this.ihiwUserRepository = ihiwUserRepository;
        this.validationRepository = validationRepository;
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
        List<IhiwUser> colleages = ihiwUserRepository.findByLab(currentIhiwUser.getLab());

        Page<UploadDTO> page;
        Pageable pageable;
        if(offset != null && size != null) {
            Sort sorting  = Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]);
            pageable = PageRequest.of(offset, size, sorting);
        } else {
            pageable = Pageable.unpaged();
        }

        if (currentUser.get().getAuthorities().contains(new Authority(ADMIN))) {
            page = userService.getAllUploads(pageable);
        } else {
            page = userService.getAllUploadsByUserId(pageable, colleages);
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

    @PutMapping("/uploads/makeentry")
    @PreAuthorize("hasRole(\"" + VALIDATION + "\")")
    public ResponseEntity<NewEntry> makeEntry(@RequestBody Upload upload) throws URISyntaxException {

        log.debug("REST request to make an entry for Upload : {}", upload.getFileName());

        List<Upload> allUploads = uploadRepository.findByFileName(upload.getFileName());
        NewEntry fileNewEntry = null;
        //two entries of this file exist, something is going wrong
        if (allUploads.size() > 1) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, upload.getFileName())).build();
        }
        //one entry exists, everything seems ok
        else if (allUploads.size() == 1) {
            fileNewEntry = new NewEntry(upload);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, upload.getFileName()))
                .body(fileNewEntry);
        }
        //no entry exists lets make it
        else {

            Connection conn = null;
            Statement stmt = null;

            try {
                //STEP 2: Register JDBC driver
                Class.forName(JDBC_DRIVER);

                //STEP 3: Open a connection
                System.out.println("Connecting to a selected database...");
                conn = DriverManager.getConnection(
                    JDBC_DRIVER, USER, PASS);
                System.out.println("Connected database successfully...");

                //STEP 4: Execute a query
                System.out.println("Creating entry in given table...");
                stmt = conn.createStatement();


                // this statement creates the correct id value for the upcoming insert. Combination of
                //https://stackoverflow.com/questions/3552260/plsql-jdbc-how-to-get-last-row-id
                // and https://stackoverflow.com/questions/6881424/how-can-i-select-the-row-with-the-highest-id-in-mysql/20904650#:~:text=The%20LIMIT%20clause%20can%20be,returned%20by%20the%20SELECT%20statement.&text=SELECT%20*%20FROM%20permlog%20WHERE%20id,not%20constrained%20to%20be%20unique.
                String sql = "SELECT MAX(ID) FROM `ihiwmanagement`.`upload` ";
                ResultSet rs = stmt.executeQuery(sql);
                int newid = 0;
                while(rs.next()){
                    newid = (rs.getInt(1));
                }

                newid++;


                //this statement actually inserts the new entry
                sql = String.format("INSERT INTO `ihiwmanagement`.`upload` "
                    + "(`id`, `type`, `created_at`, `modified_at`) "
                    + "VALUES "
                    + "%d %s %s %s ",newid ,upload.getType(),upload.getCreatedAt(),upload.getModifiedAt());


                stmt.executeUpdate(sql);
                System.out.println("Created entry in given table...");
            } catch (SQLException se) {
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (Exception e) {
                //Handle errors for Class.forName
                e.printStackTrace();
            } finally {
                //finally block used to close resources
                try {
                    if (stmt != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                }// do nothing
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }//end finally try
            }//end try


            log.debug("Entry added:" + fileNewEntry.getName());
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fileNewEntry.getName()))
                .body(fileNewEntry);
        }
    }

    @PutMapping("/uploads/copyupload")
    @PreAuthorize("hasRole(\"" + VALIDATION + "\")")
    public ResponseEntity<Upload> copyUpload(@RequestParam(required = true) String oldfileName, @RequestParam(required = true) FileType newType) throws URISyntaxException {

        log.debug("REST request to make an entry for Upload : {}", oldfileName);

        List<Upload> allUploads = uploadRepository.findByFileName(oldfileName);
        Upload result = null;

        if (allUploads.isEmpty())
        {
            return ResponseEntity.notFound().headers(HeaderUtil.createAlert(applicationName,  ENTITY_NAME, oldfileName)).build();
        }

        Upload oldUpload = allUploads.get(0);  //fetch the csv upload, like in setUploadValidation.



        //two entries of this file exist, something is going wrong
        if (allUploads.size() > 1) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oldUpload.getFileName())).build();
        }
        //one entry exists, everything seems ok
        else if (allUploads.size() == 1) {
            Upload currentUpload = new Upload(); //Not sure this will work..but I think so.
            int iend = oldUpload.getFileName().indexOf("."); //this finds the first occurrence of "."
            String newName = null;
            if (iend != -1)
            {
                newName = oldUpload.getFileName().substring(0 , iend) + "." + newType.toString() ; //this will give abc
            }
            else {
                newName = oldUpload.getFileName()+ "." + newType.toString() ;
            }
            currentUpload.setFileName(newName); // like in createUpload
            IhiwUser currentUser = oldUpload.getCreatedBy();
            currentUpload.setCreatedBy(currentUser);
            currentUpload.setCreatedAt(ZonedDateTime.now());
            currentUpload.setModifiedAt(ZonedDateTime.now());
            currentUpload.setEnabled(oldUpload.getEnabled());
            currentUpload.setType(newType);
            result = uploadRepository.save(currentUpload);
            return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oldUpload.getFileName()))
                .body(result);
        }
        else {
            return ResponseEntity.noContent().headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, oldUpload.getFileName())).build();
        }
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
