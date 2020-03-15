package com.softserve.rms.dto.user;

import com.softserve.rms.validator.PhoneExist;
import com.softserve.rms.constants.ValidationErrorConstants;
import com.softserve.rms.constants.ValidationPattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Optional;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDto {
    private Optional<String> firstName;

    private Optional<String> lastName;

    private Optional<String> phone;
}
