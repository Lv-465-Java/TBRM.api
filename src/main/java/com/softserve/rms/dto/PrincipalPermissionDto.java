package com.softserve.rms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrincipalPermissionDto {
    private String principal;
    private String permission;
}
