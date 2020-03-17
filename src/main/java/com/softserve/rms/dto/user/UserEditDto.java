package com.softserve.rms.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDto {
    private Optional<String> firstName;

    private Optional<String> lastName;

    private Optional<String> phone;
}
