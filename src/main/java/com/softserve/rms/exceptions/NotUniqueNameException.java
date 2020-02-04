package com.softserve.rms.exceptions;

/**
 * Exception that we get when user tries to add existed name to resource parameter
 * or resource template.
 *
 * @author Andrii Bren
 */
public class NotUniqueNameException extends RuntimeException {

    /**
     * Constructor for NotUniqueNameException.
     *
     * @param message - giving message
     */
    public NotUniqueNameException(String message) {
        super(message);
    }
}
