package org.ihiw.management.repository;

import org.ihiw.management.domain.Upload;
import org.ihiw.management.domain.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ValidationRepository extends JpaRepository<Validation, Long> {
    List<Validation> findByUpload(Upload upload);
}
