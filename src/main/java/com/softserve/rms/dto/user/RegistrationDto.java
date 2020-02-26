package com.softserve.rms.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegistrationDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String tenantid;
}
