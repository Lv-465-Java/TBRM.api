package com.softserve.rms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
}
