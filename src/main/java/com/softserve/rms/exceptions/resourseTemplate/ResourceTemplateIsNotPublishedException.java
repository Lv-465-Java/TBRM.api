package com.softserve.rms.exceptions.resourseTemplate;

/**
 * Exception that we get when user tries to make a reference on a Resource Template entity, which is not published yet.
 *
 * @author Halyna Yatseniuk
 */
public class ResourceTemplateIsNotPublishedException extends RuntimeException {

    /**
     * Constructor for ResourceTemplateIsNotPublishedException.
     *
     * @param message - giving message
     */
    public ResourceTemplateIsNotPublishedException(String message) {
        super(message);
    }
}