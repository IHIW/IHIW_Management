package org.ihiw.management.repository;

import org.ihiw.management.domain.IhiwUser;
import org.ihiw.management.domain.Upload;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Upload entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UploadRepository extends JpaRepository<Upload, Long> {
    List<Upload> findByCreatedByIn(List<IhiwUser> users);
}
