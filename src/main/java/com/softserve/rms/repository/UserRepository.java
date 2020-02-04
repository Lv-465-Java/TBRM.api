package com.softserve.rms.repository;

import com.softserve.rms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Save person
     *
     * @param user to save.
     * @return Person saved}
     */
    User save(User user);

    /**
     * Find Person by email.
     *
     * @param email person email.
     * @return Person
     */
    User findByEmail(String email);

    /**
     * Method that check if person with this email already exist
     *
     * @param email
     * @author Mariia Shchur
     */
    boolean existsPersonByEmail(String email);

    /**
     * Method that check if person with this phone number already exist
     *
     * @param phone
     * @author Mariia Shchur
     */
    boolean existsPersonByPhone(String phone);


}
