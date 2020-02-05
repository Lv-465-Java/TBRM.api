package com.softserve.rms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that we get when jwt authentication failed.
 * @author Kravets Maryana
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JwtAuthenticationException extends RuntimeException {

    /**
     * Constructor for JwtAuthenticationException.
     *
     * @param msg - giving message.
     */
    public JwtAuthenticationException(String msg){
        super(msg);
    }
}
