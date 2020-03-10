package com.softserve.rms.dto;

import com.softserve.rms.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String imageUrl;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private boolean enabled;
    private Role role;
}