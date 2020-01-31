package com.softserve.rms.exceptions.resourseTemplate;

/**
 * Exception that we get when user tries to modify a Resource Template entity, which does not exist.
 *
 * @author Halyna Yatseniuk
 */
public class NoSuchResourceTemplateException extends RuntimeException {

    /**
     * Constructor for NoSuchResourceTemplateException.
     *
     * @param message - giving message
     */
    public NoSuchResourceTemplateException(String message) {
        super(message);
    }
}