package com.softserve.rms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PermissionDto {

    private Long resTempId;
    private String recipient;
    private String permission;
    private boolean principal;

}
