package com.softserve.rms.exceptions;

public class JwtAuthenticationException extends RuntimeException {

    public JwtAuthenticationException(String msg){
        super(msg);
    }
}
