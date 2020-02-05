package com.softserve.rms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that we get when refresh token.
 * @author Kravets Maryana
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RefreshTokenException extends RuntimeException{

    /**
     * Constructor for RefreshTokenException.
     *
     * @param msg - giving message.
     */
    public RefreshTokenException(String msg){
        super(msg);
    }
}
