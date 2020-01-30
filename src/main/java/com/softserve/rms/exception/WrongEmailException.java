package com.softserve.rms.exception;

public class WrongEmailException extends RuntimeException{
    public WrongEmailException(String message) {
        super(message);
    }
}
