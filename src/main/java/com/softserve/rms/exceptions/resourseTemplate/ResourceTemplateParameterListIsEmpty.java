package com.softserve.rms.exceptions.resourseTemplate;

/**
 * Exception that we get when user tries to publish a Resource Template entity, which does not have any parameter.
 *
 * @author Halyna Yatseniuk
 */
public class ResourceTemplateParameterListIsEmpty extends RuntimeException {

    /**
     * Constructor for ResourceTemplateParameterListIsEmpty.
     *
     * @param message - giving message
     */
    public ResourceTemplateParameterListIsEmpty(String message) {
        super(message);
    }
}