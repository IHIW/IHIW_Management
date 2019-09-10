package org.ihiw.management.repository;

import org.ihiw.management.domain.IhiwLab;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the IhiwLab entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IhiwLabRepository extends JpaRepository<IhiwLab, Long> {
    Optional<IhiwLab> findByLabCode(String labCode);
}
