package com.softserve.rms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidParametersException extends RuntimeException {
    public InvalidParametersException(String msg) {
        super(msg);
    }
}