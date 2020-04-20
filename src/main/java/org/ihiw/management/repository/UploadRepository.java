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
    Page<Upload> findByCreatedByIn(List<IhiwUser> users, Pageable pageable);


    Optional<Upload> findById(Long id);

    Page<Upload> findAllById(Pageable pageable, Long id);

    @Query("select upload from Upload upload")
    Page<Upload> findAll(Pageable pageable);

    @Query("select upload from Upload upload WHERE upload.id in (:ids)")
    Page<Upload> findAllByUserId(Pageable pageable,@Param("ids") List<Long> ids);

}
