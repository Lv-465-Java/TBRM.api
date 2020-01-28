package com.softserve.rms.exception;

public class PasswordsDoNotMatchesException extends RuntimeException{
    public PasswordsDoNotMatchesException(String message) {
        super(message);
    }
}
