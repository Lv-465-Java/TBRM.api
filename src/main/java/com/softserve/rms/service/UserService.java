package com.softserve.rms.service;

import com.softserve.rms.dto.PasswordEditDto;
import com.softserve.rms.dto.RegistrationDto;
import com.softserve.rms.dto.UserEditDto;
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

    User findByEmail(String email);

}
