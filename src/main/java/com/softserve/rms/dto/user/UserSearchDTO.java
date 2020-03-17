package com.softserve.rms.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean enabled;
    private String role;
}