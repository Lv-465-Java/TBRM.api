package com.softserve.rms.exceptions.user;

public class WrongEmailException extends RuntimeException{
    public WrongEmailException(String message) {
        super(message);
    }
}
