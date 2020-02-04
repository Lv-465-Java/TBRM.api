package com.softserve.rms.dto;

import com.softserve.rms.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoRole {

    private Role role;
}

