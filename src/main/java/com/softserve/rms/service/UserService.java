package com.softserve.rms.service;

import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.RegistrationDto;
import com.softserve.rms.dto.user.UserEditDto;
import com.softserve.rms.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    /**
     * Method saves {@link User}.
     *
     * @param person {@link RegistrationDto}
     * @return boolean
     * @author Mariia Shchur
     */
    void save(RegistrationDto person);

    /**
     * Method update {@link User}.
     *
     * @param userEditDto {@link UserEditDto},currentUserEmail
     * @author Mariia Shchur
     */
    void update(UserEditDto userEditDto, String currentUserEmail);

    /**
     * Method update user's password.
     *
     * @param passwordEditDto {@link PasswordEditDto},currentUserEmail
     * @author Mariia Shchur
     */
    void editPassword(PasswordEditDto passwordEditDto, String currentUserEmail);

    /**
     * Method that allow you to get {@link User} by email.
     *
     * @param email a value of {@link String}
     * @return {@link User}
     */
    User getUserByEmail(String email);

    /**
     * Method that allow you to get {@link User} by ID.
     *
     * @param id a value of {@link Long}
     * @return {@link User}
     */
    User getById(long id);
}