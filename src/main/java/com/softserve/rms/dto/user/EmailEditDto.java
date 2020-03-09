package com.softserve.rms.dto.user;

import com.softserve.rms.validator.EmailExist;
import com.softserve.rms.constants.ValidationErrorConstants;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class EmailEditDto {
    @NotEmpty(message = ValidationErrorConstants.EMPTY_PASSWORD)
    private String password;

    @EmailExist
    @NotBlank(message = ValidationErrorConstants.EMPTY_EMAIL)
    @Email(message = ValidationErrorConstants.INVALID_EMAIL)
    private String email;
}
