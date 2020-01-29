package com.softserve.rms.exceptions;

public class BadCredentialException extends RuntimeException {

    public BadCredentialException(String msg){
        super(msg);
    }
}
