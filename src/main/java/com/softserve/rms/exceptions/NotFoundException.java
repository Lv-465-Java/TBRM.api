package com.softserve.rms.exceptions;

/**
 * Exception that we get when user tries to modify Entity, which does not exist.
 *
 * @author Andrii Bren
 */
public class NotFoundException extends RuntimeException {

    /**
     * Constructor for NotFoundException.
     *
     * @param message - giving message
     */
    public NotFoundException(String message) {
        super(message);
    }
}
