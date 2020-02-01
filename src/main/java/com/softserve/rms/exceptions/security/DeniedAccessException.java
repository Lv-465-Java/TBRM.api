package com.softserve.rms.exceptions.security;

/**
 * Exception that we get when user tries to delete Permission
 * and he isn`t MANAGER.
 *
 * @author Artur Sydor
 */
public class DeniedAccessException extends RuntimeException {
    /**
     * Constructor with parameters.
     *
     * @param message - given message
     */
    public DeniedAccessException(String message) {
        super(message);
    }
}
