package com.softserve.rms.service;

import com.softserve.rms.dto.PasswordEditDto;
import com.softserve.rms.dto.RegistrationDto;
import com.softserve.rms.dto.UserEditDto;
import com.softserve.rms.entities.Person;
import org.springframework.stereotype.Service;

@Service
public interface PersonService {
    /**
     * Method saves {@link Person}.
     *
     * @param person {@link RegistrationDto}
     * @return boolean
     * @author Mariia Shchur
     */
    boolean save(RegistrationDto person);

    /**
     * Method update {@link Person}.
     *
     * @param userEditDto {@link UserEditDto},email
     * @author Mariia Shchur
     */
    void update(UserEditDto userEditDto, String email);
    void editPassword(PasswordEditDto passwordEditDto, String currentUserEmail);

}
