package com.softserve.rms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtDto {

    private String accessToken;
    private String refreshToken;
}
