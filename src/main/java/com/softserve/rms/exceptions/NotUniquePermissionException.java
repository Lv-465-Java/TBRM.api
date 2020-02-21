package com.softserve.rms.exceptions;

public class NotUniquePermissionException extends RuntimeException {
    public NotUniquePermissionException(String message) {
        super(message);
    }
}
