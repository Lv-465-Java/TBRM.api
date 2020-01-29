package com.softserve.rms.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String msg){
        super(msg);
    }
}
