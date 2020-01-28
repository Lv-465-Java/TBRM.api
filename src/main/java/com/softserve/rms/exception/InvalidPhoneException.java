package com.softserve.rms.exception;

public class InvalidPhoneException extends RuntimeException{
    public InvalidPhoneException(String message) {
        super(message);
    }
}
