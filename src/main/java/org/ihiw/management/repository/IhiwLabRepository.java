package org.ihiw.management.repository;

import org.ihiw.management.domain.IhiwLab;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the IhiwLab entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IhiwLabRepository extends JpaRepository<IhiwLab, Long> {
    Optional<IhiwLab> findByLabCode(String LabCode);

    Optional<IhiwLab> findById(Long id);

    Page<IhiwLab> findAllById(Pageable pageable, Long id);

    @Query("select lab from IhiwLab lab")
    Page<IhiwLab> findAll(Pageable pageable);


    @Query("select lab from IhiwLab lab WHERE lab.id in (:ids)")
    Page<IhiwLab> findByLabInIds(Pageable pageable, @Param("ids") List<Long> ids);
}




