package com.softserve.rms.exceptions;

public class JwtExpiredTokenException extends RuntimeException {

    public JwtExpiredTokenException(String msg){
        super(msg);
    }
}
