package com.softserve.rms.service;

import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.RegistrationDto;
import com.softserve.rms.dto.user.UserEditDto;
import org.springframework.stereotype.Service;

@Service
public interface UserValidationService {

    /**
     * Method that check if all field in RegistrationDto are valid
     *
     * @param person value of {@link RegistrationDto}
     * @return {@link RegistrationDto}.
     * @author Mariia Shchur
     */
    RegistrationDto validate(RegistrationDto person);

    /**
     * Method that check if all field in UserEditDto are valid
     *
     * @param user value of {@link UserEditDto}
     * @return {@link UserEditDto}.
     * @author Mariia Shchur
     */
    UserEditDto validateUpdateData(UserEditDto user);

    /**
     * Method that check if all field in PasswordEditDto are valid
     *
     * @param passwordEditDto value of {@link PasswordEditDto}
     * @return {@link PasswordEditDto}.
     * @author Mariia Shchur
     */
    void validatePassword(PasswordEditDto passwordEditDto);
}
