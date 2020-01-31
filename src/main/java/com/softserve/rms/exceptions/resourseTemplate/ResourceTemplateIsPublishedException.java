package com.softserve.rms.exceptions.resourseTemplate;

/**
 * Exception that we get when user tries to publish a Resource Template entity, which is already published.
 *
 * @author Halyna Yatseniuk
 */
public class ResourceTemplateIsPublishedException extends RuntimeException {

    /**
     * Constructor for ResourceTemplateIsPublishedException.
     *
     * @param message - giving message
     */
    public ResourceTemplateIsPublishedException(String message) {
        super(message);
    }
}