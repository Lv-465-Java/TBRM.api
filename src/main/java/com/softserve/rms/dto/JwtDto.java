package com.softserve.rms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class with save access and refresh token
 * @author Kravets Maryana
 */
@Data
@AllArgsConstructor
public class JwtDto {

    private String accessToken;
    private String refreshToken;
}
