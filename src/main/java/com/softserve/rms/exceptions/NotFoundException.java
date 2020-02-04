package com.softserve.rms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that we get when not found.
 * @author Kravets Maryana
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    /**
     * Constructor for NotFoundException.
     *
     * @param msg - giving message.
     */
    public NotFoundException(String msg){
        super(msg);
    }
}
