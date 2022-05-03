package org.ihiw.management.repository;

import org.ihiw.management.domain.IhiwLab;
import org.ihiw.management.domain.IhiwUser;
import org.ihiw.management.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the IhiwUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IhiwUserRepository extends JpaRepository<IhiwUser, Long> {
    @Query("select ihiwUser from IhiwUser ihiwUser where ihiwUser.user.login = ?#{principal.username}")
    IhiwUser findByUserIsCurrentUser();

    IhiwUser findByUser(User user);

    @Override
    @Query("select ihiwUser from IhiwUser ihiwUser left join fetch ihiwUser.projectLeaderships where ihiwUser.id = :id")
    Optional<IhiwUser> findById(@Param("id") Long id);

    List<IhiwUser> findByLab(IhiwLab ihiwLab);



}
