package com.softserve.rms.exceptions.resourseTemplate;

/**
 * Exception that we get when user tries to save a Resource Template or Parameter entity with name,
 * which has been already used.
 *
 * @author Halyna Yatseniuk
 */
public class NameIsNotUniqueException extends RuntimeException {

    /**
     * Constructor for NameIsNotUniqueException.
     *
     * @param message - giving message
     */
    public NameIsNotUniqueException(String message) {
        super(message);
    }
}