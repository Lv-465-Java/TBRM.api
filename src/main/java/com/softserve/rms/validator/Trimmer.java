package com.softserve.rms.validator;

import com.softserve.rms.dto.user.RegistrationDto;
import com.softserve.rms.dto.user.UserEditDto;
import org.springframework.stereotype.Service;

@Service
public class Trimmer {

    /**
     * Method that trim registrationDto data and set email to lower case
     *
     * @param registrationDto value of {@link RegistrationDto}
     * @return {@link RegistrationDto}.
     * @author Mariia Shchur
     */
    public RegistrationDto trimRegistrationData(RegistrationDto registrationDto) {
        return new RegistrationDto(registrationDto.getFirstName().trim(),
                registrationDto.getLastName().trim(),
                registrationDto.getEmail().toLowerCase().trim(),
                registrationDto.getPhone().trim(),
                registrationDto.getPassword());
    }

    /**
     * Method that trim userEditDto data and set email to lower case
     *
     * @param userEditDto value of {@link UserEditDto}
     * @return {@link UserEditDto}.
     * @author Mariia Shchur
     */
    public UserEditDto trimEditData(UserEditDto userEditDto) {
        return new UserEditDto(userEditDto.getFirstName().trim(),
                userEditDto.getLastName().trim(),
                userEditDto.getPhone().trim());
    }
}
