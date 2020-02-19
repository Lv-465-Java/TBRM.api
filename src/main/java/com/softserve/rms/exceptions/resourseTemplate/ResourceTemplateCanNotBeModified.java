package com.softserve.rms.exceptions.resourseTemplate;

/**
 * Exception that we get when user tries to modify a Resource Template entity, which is published.
 *
 * @author Halyna Yatseniuk
 */
public class ResourceTemplateCanNotBeModified extends RuntimeException {

    /**
     * Constructor for ResourceTemplateCanNotBeModified.
     *
     * @param message - giving message
     */
    public ResourceTemplateCanNotBeModified(String message) {
        super(message);
    }
}