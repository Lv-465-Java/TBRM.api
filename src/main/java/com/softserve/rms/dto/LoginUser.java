package com.softserve.rms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  Class which contain user credential
 * @author Kravets Maryana
 */
@Data
@AllArgsConstructor
public class LoginUser {

    private String email;
    private String password;
}
