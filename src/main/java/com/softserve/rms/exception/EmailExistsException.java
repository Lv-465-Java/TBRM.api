package com.softserve.rms.exception;

public class EmailExistsException extends RuntimeException  {

    public EmailExistsException(String message) {

        super(message);
    }
}
