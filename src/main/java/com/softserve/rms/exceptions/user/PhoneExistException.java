package com.softserve.rms.exceptions.user;

public class PhoneExistException  extends RuntimeException{
    public PhoneExistException(String message) {
        super(message);
    }
}
