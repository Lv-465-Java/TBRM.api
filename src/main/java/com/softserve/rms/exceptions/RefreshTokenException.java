package com.softserve.rms.exceptions;

public class RefreshTokenException extends RuntimeException{

    public RefreshTokenException(String msg){
        super(msg);
    }
}
