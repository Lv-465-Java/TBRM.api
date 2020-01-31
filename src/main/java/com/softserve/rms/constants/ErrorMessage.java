package com.softserve.rms.constants;

public enum ErrorMessage {
    CAN_NOT_FIND_A_RESOURCE_TEMPLATE("There is no such a resource template"),
    RESOURCE_TEMPLATE_IS_ALREADY_PUBLISHED("Resource template is already published"),
    RESOURCE_TEMPLATE_IS_NOT_PUBLISHED("You can't add relation to the resource template, " +
            "which has not been published yet"),
    RESOURCE_TEMPLATE_DO_NOT_HAVE_ANY_PARAMETERS("Resource template must consist of at least one parameter"),
    RESOURCE_TEMPLATE_NAME_IS_NOT_UNIQUE("Resource template name should be unique"),
    RESOURCE_PARAMETER_CAN_NOT_BE_FOUND_BY_ID("Resource parameter does not exist with id:  "),
    RESOURCE_TEMPLATE_HAS_NOT_ANY_PARAMETERS("There is no parameters for template with id: "),
    RESOURCE_PARAMETER_CAN_NOT_BE_DELETE_BY_ID("Resource parameter does not deleted with id: "),
    RESOURCE_PARAMETER_IS_ALREADY_EXISTED("Resource parameter is already existed with name: ");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}