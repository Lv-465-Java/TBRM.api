package com.softserve.rms.dto.user;

import com.softserve.rms.validator.EmailExist;
import com.softserve.rms.validator.PhoneExist;
import com.softserve.rms.constants.ValidationErrorConstants;
import com.softserve.rms.constants.ValidationPattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegistrationDto implements Serializable {

    @NotBlank(message =  ValidationErrorConstants.EMPTY_FIRSTNAME)
    @Pattern(regexp = ValidationPattern.NAME_PATTERN,
            message = ValidationErrorConstants.INVALID_FIRSTNAME)
    private String firstName;

    @NotBlank(message = ValidationErrorConstants.EMPTY_LASTNAME)
    @Pattern(regexp = ValidationPattern.NAME_PATTERN,
            message = ValidationErrorConstants.INVALID_LASTNAME)
    private String lastName;

    @NotBlank(message =ValidationErrorConstants.EMPTY_EMAIL )
    @Pattern(regexp = ValidationPattern.EMAIL_PATTERN,
            message = ValidationErrorConstants.INVALID_EMAIL)
    @EmailExist
    private String email;


    @NotBlank(message = ValidationErrorConstants.EMPTY_PHONE)
    @Pattern(regexp = ValidationPattern.PHONE_PATTERN,
            message = ValidationErrorConstants.INVALID_PHONE)
    @PhoneExist
    private String phone;

    @NotEmpty(message = ValidationErrorConstants.EMPTY_PASSWORD)
    @Pattern(regexp = ValidationPattern.PASSWORD_PATTERN,
            message = ValidationErrorConstants.INVALID_PASSWORD)
    private String password;
}
