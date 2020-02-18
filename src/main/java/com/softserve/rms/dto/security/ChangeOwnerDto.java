package com.softserve.rms.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ChangeOwnerDto {

    private Long resTempId;
    private String recipient;
}
