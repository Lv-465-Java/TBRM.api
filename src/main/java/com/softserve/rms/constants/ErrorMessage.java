package com.softserve.rms.constants;

public enum ErrorMessage {
    CAN_NOT_FIND_A_RESOURCE_TEMPLATE("There is no such a resource template"),
    RESOURCE_TEMPLATE_IS_ALREADY_PUBLISHED("Resource template is already published"),
    RESOURCE_TEMPLATE_DO_NOT_HAVE_ANY_PARAMETERS("Resource template must consist of at least one parameter"),
    RESOURCE_TEMPLATE_NAME_IS_NOT_UNIQUE("Resource template name should be unique"),
    RESOURCE_PARAMETER_CAN_NOT_FOUND_BY_ID("Resource parameter does not exist with id:  "),
    RESOURCE_TEMPLATE_HAS_NOT_ANY_PARAMETERS("There is no parameters for template with id: "),
    RESOURCE_PARAMETER_CAN_NOT_DELETE_BY_ID("Resource parameter does not deleted with id: ");

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