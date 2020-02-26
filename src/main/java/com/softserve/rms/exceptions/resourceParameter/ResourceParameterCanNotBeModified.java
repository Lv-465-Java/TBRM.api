package com.softserve.rms.exceptions.resourceParameter;

/**
 * Exception that we get when user tries to modify a Resource Parameter entity, which is related to
 * published Resource Template.
 *
 * @author Halyna Yatseniuk
 */
public class ResourceParameterCanNotBeModified extends RuntimeException {
    /**
     * Constructor for ResourceParameterCanNotBeModified.
     *
     * @param message - giving message
     */
    public ResourceParameterCanNotBeModified(String message) {
        super(message);
    }
}