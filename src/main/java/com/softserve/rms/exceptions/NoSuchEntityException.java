package com.softserve.rms.exceptions;

/**
 * Exception that we get when user trying to modify an entity, which does not exist.
 *
 * @author Halyna Yatseniuk
 */

public class NoSuchEntityException extends RuntimeException {

    /**
     * Constructor for NoSuchEntityException.
     *
     * @param message - giving message
     */
    public NoSuchEntityException(String message) {
        super(message);
    }
}