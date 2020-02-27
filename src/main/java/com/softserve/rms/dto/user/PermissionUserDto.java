package com.softserve.rms.dto.user;

import com.softserve.rms.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PermissionUserDto {

    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
