package org.ihiw.management.repository;

import org.ihiw.management.domain.IhiwLab;
import org.ihiw.management.domain.IhiwUser;
import org.ihiw.management.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the IhiwUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IhiwUserRepository extends JpaRepository<IhiwUser, Long> {
    @Query("select ihiwUser from IhiwUser ihiwUser where ihiwUser.user.login = ?#{principal.username}")
    IhiwUser findByUserIsCurrentUser();

    IhiwUser findByUser(User user);

    List<IhiwUser> findByLab(IhiwLab ihiwLab);
}
