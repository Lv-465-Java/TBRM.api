package com.softserve.rms.dto.user;

import com.softserve.rms.validator.PhoneExist;
import com.softserve.rms.constants.ValidationErrorConstants;
import com.softserve.rms.constants.ValidationPattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDto {
    @NotBlank(message = ValidationErrorConstants.EMPTY_FIRSTNAME)
    @Pattern(regexp = ValidationPattern.NAME_PATTERN,
            message = ValidationErrorConstants.INVALID_FIRSTNAME)
    private String firstName;

    @NotBlank(message = ValidationErrorConstants.EMPTY_LASTNAME)
    @Pattern(regexp = ValidationPattern.NAME_PATTERN,
            message = ValidationErrorConstants.INVALID_LASTNAME)
    private String lastName;

    @PhoneExist
    @NotBlank(message = ValidationErrorConstants.EMPTY_PHONE)
    @Pattern(regexp = ValidationPattern.PHONE_PATTERN,
            message = ValidationErrorConstants.INVALID_PHONE)
    private String phone;
}
