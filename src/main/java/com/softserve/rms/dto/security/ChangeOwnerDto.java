package com.softserve.rms.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeOwnerDto {

    private Long id;
    private String recipient;
}
