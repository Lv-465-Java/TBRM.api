package com.softserve.rms.dto.user;

import com.softserve.rms.constants.ValidationErrorConstants;
import com.softserve.rms.constants.ValidationPattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordEditDto {


    @NotEmpty(message = ValidationErrorConstants.EMPTY_PASSWORD)
    private String oldPassword;

    @NotEmpty(message = ValidationErrorConstants.EMPTY_PASSWORD)
    @Pattern(regexp = ValidationPattern.PASSWORD_PATTERN,
            message = ValidationErrorConstants.INVALID_PASSWORD)
    private String newPassword;

}
