package com.softserve.rms.repository;

import com.softserve.rms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Provides an interface to manage {@link User} entity.
 * @author Kravets Maryana
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * Find {@link User} by email.
     *
     * @param email user email.
     * @return Optional of {@link User}
     */
    Optional<User> findByEmail(String email);

    /**
     * Find {@link User} by id.
     *
     * @param id is value of {@link Long}.
     * @return Optional of {@link User}
     */
    Optional<User> findById(long id);

}
