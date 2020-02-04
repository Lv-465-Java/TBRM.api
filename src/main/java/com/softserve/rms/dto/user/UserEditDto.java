package com.softserve.rms.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
