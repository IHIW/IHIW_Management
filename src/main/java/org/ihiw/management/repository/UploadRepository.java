package org.ihiw.management.repository;

import org.ihiw.management.domain.IhiwUser;
import org.ihiw.management.domain.Upload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Upload entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UploadRepository extends JpaRepository<Upload, Long> {
    List<Upload> findByCreatedByIn(List<IhiwUser> users);
    List<Upload> findByFileName(String filename);
    
    @Query("select upload from Upload upload where upload.createdBy in ?1 and upload.parentUpload.id is null")
    Page<Upload> findParentlessByCreatedByIn(List<IhiwUser> users, Pageable pageable);

    @Query("select upload from Upload upload where upload.parentUpload.id is null and (upload.createdBy.id in ?1 or upload.project.id in ?2)")
    Page<Upload> findParentlessByUsersAndProjects(List<Long> userIds, List<Long> projectIds, Pageable pageable);

    Optional<Upload> findById(Long id);

    List<Upload> findAll();

    List<Upload> findAllById(List<Long> ids);
    
    List<Upload> findAllByProjectId(Long projectId);
    
    @Query("select upload from Upload upload where upload.parentUpload.id = ?1")
    List<Upload> findChildrenById(Long id);

    @Query("select upload from Upload upload where upload.parentUpload.id is null")
    Page<Upload> findParentless(Pageable pageable);
}
