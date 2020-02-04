package com.softserve.rms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that we get when jwt token expired.
 * @author Kravets Maryana
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class JwtExpiredTokenException extends RuntimeException {

    /**
     * Constructor for JwtExpiredTokenException.
     *
     * @param msg - giving message.
     */
    public JwtExpiredTokenException(String msg){
        super(msg);
    }
}
