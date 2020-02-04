package com.softserve.rms.exceptions;

/**
 * Exception that we get when user tries to delete Permission
 * and he isn`t MANAGER.
 *
 * @author Artur Sydor
 */
public class PermissionException extends RuntimeException {
    /**
     * Constructor with parameters.
     *
     * @param message - given message
     */
    public PermissionException(String message) {
        super(message);
    }
}
