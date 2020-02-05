package com.softserve.rms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that we get when user enter bad credential.
 * @author Kravets Maryana
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadCredentialException extends RuntimeException {

    /**
     * Constructor for BadCredentialException.
     *
     * @param msg - giving message.
     */
    public BadCredentialException(String msg){
        super(msg);
    }
}
