package org.ihiw.management.service;

import org.ihiw.management.config.Constants;
import org.ihiw.management.domain.*;
import org.ihiw.management.repository.*;
import org.ihiw.management.security.AuthoritiesConstants;
import org.ihiw.management.security.SecurityUtils;
import org.ihiw.management.service.dto.LabDTO;
import org.ihiw.management.service.dto.ProjectDTO;
import org.ihiw.management.service.dto.UploadDTO;
import org.ihiw.management.service.dto.UserDTO;
import org.ihiw.management.service.util.RandomUtil;
import org.ihiw.management.web.rest.errors.EmailAlreadyUsedException;
import org.ihiw.management.web.rest.errors.InvalidPasswordException;
import org.ihiw.management.web.rest.errors.LabDoesNotExistException;
import org.ihiw.management.web.rest.errors.LoginAlreadyUsedException;
import org.ihiw.management.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UploadService {

    private final Logger log = LoggerFactory.getLogger(UploadService.class);

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    private final IhiwUserRepository ihiwUserRepository;

    private final IhiwLabRepository ihiwLabRepository;

    private final UploadRepository uploadRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    public UploadService(UploadRepository uploadRepository, UserRepository userRepository, ProjectRepository projectRepository, IhiwUserRepository ihiwUserRepository, IhiwLabRepository ihiwLabRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.ihiwLabRepository = ihiwLabRepository;
        this.ihiwUserRepository = ihiwUserRepository;
        this.projectRepository = projectRepository;
        this.uploadRepository = uploadRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }


    public Upload createUpload(UploadDTO uploadDTO) {
        Upload upload = new Upload();

        upload.setId(uploadDTO.getId());
        upload.setValidations(uploadDTO.getValidations());
        upload.setCreatedBy(uploadDTO.getCreatedBy());
        upload.setCreatedAt(uploadDTO.getCreatedAt());
        upload.setProject(uploadDTO.getProject());
        upload.setParentUpload(uploadDTO.getParentUpload());
        upload.setFileName(uploadDTO.getFileName());
        upload.setType(uploadDTO.getType());
        upload.setEnabled(uploadDTO.isEnabled());

        uploadRepository.save(upload);

        log.debug("Created Information for Upload: {}", upload);
        return upload;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param uploadDTO user to update.
     * @return updated upload.
     */
    public Optional<UploadDTO> updateUpload(UploadDTO uploadDTO) {
        return Optional.of(uploadRepository
            .findById(uploadDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(upload -> {
                upload.setParentUpload(uploadDTO.getParentUpload());
                upload.setProject(uploadDTO.getProject());
                upload.setEnabled(uploadDTO.isEnabled());
                upload.setCreatedAt(uploadDTO.getCreatedAt());
                upload.setFileName(uploadDTO.getFileName());
                upload.setId(uploadDTO.getId());
                upload.setCreatedBy(uploadDTO.getCreatedBy());
                upload.setModifiedAt(uploadDTO.getModifiedAt());
                upload.setType(uploadDTO.getType());
                log.debug("Changed Information for Upload: {}", uploadDTO);
                return upload;
            })
            .map(UploadDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UploadDTO> getParentlessUploads(Pageable pageable) {
        return  uploadRepository.findParentless(pageable).map(UploadDTO::new);
    }
    
    @Transactional(readOnly = true)
    public Page<UploadDTO> getParentlessUploadsByUserId(Pageable pageable, List<IhiwUser> users) {
        return  uploadRepository.findParentlessByCreatedByIn(users, pageable).map(UploadDTO::new);
    }
    
    public List<Upload> getAllUploadsByParentId(long parentId) { 	
    	List<Upload> childUploads = uploadRepository.findChildrenById(parentId);     	
    	return childUploads;
    }

    @Transactional(readOnly = true)
    public Page<UploadDTO> getParentlessUploadsByUsersAndProjects(Pageable pageable, List<IhiwUser> users, List<Project> projects) {

    	List<Long> userIds = new ArrayList<Long>();
        for (IhiwUser ihiwUser : users) {
        	userIds.add(ihiwUser.getId());
        }
       	List<Long> projectIds = new ArrayList<Long>();
        for (Project project : projects) {
        	projectIds.add(project.getId());
        }
        return  uploadRepository.findParentlessByUsersAndProjects(userIds, projectIds, pageable).map(UploadDTO::new);
    }

}
