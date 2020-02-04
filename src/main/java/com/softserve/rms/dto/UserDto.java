package com.softserve.rms.dto;

import com.softserve.rms.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String firstName;
    private boolean enabled;
    private Role role;
    private String lastName;
    private String email;
    private String phone;
    private String password;

}
