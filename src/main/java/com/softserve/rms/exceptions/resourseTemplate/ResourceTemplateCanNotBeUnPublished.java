package com.softserve.rms.exceptions.resourseTemplate;

/**
 * Exception that we get when user tries to cancel a publish of a Resource Template entity,
 * but Resource Template container table has records.
 *
 * @author Halyna Yatseniuk
 */
public class ResourceTemplateCanNotBeUnPublished extends RuntimeException {
    /**
     * Constructor for ResourceTemplateCanNotBeUnPublished.
     *
     * @param message - giving message
     */
    public ResourceTemplateCanNotBeUnPublished(String message) {
        super(message);
    }
}