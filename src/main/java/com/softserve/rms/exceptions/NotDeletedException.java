package com.softserve.rms.exceptions;

/**
 * Exception that we get when user tries to delete Entity, which does not exist.
 *
 * @author Andrii Bren
 */
public class NotDeletedException extends RuntimeException {

    /**
     * Constructor for NotDeletedException.
     *
     * @param message - giving message
     */
    public NotDeletedException(String message) {
        super(message);
    }
}
