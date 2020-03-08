package com.softserve.rms.repository;

import com.softserve.rms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    /**
     * Save user
     *
     * @param user to save.
     * @return User saved}
     */
    User save(User user);

    /**
     * Find User by email.
     *
     * @param email user email.
     * @return user {@link User}
     */
    Optional<User> findUserByEmail(String email);

    /**
     * Method that check if user with this email already exist
     *
     * @param email
     * @author Mariia Shchur
     */
    boolean existsUserByEmail(String email);

    /**
     * Method that check if user with this phone number already exist
     *
     * @param phone
     * @author Mariia Shchur
     */
    boolean existsUserByPhone(String phone);

    @Override
    void deleteById(Long id);

    Optional<User> findUserByResetToken(String resetToken);
}
