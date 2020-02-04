package com.softserve.rms.constants;

public enum ErrorMessage {
    CAN_NOT_FIND_A_RESOURCE_TEMPLATE("There is no such a resource template"),
    RESOURCE_TEMPLATE_IS_ALREADY_PUBLISHED("Resource template is already published"),
    RESOURCE_TEMPLATE_IS_NOT_PUBLISHED("You can't add relation to the resource template, " +
            "which has not been published yet"),
    RESOURCE_TEMPLATE_DO_NOT_HAVE_ANY_PARAMETERS("Resource template must consist of at least one parameter"),
    RESOURCE_TEMPLATE_NAME_IS_NOT_UNIQUE("Resource template name should be unique"),
    RESOURCE_TEMPLATE_CAN_NOT_BE_DELETED("Resource template could not be deleted"),


    RESOURCE_PARAMETER_CAN_NOT_BE_FOUND_BY_ID("Resource parameter does not exist with id:  "),
    RESOURCE_TEMPLATE_HAS_NOT_ANY_PARAMETERS("There is no parameters for template with id: "),
    RESOURCE_PARAMETER_CAN_NOT_BE_DELETE_BY_ID("Resource parameter does not deleted with id: "),
    RESOURCE_PARAMETER_IS_ALREADY_EXISTED("Resource parameter is already existed with name: "),

    USER_WITH_EMAIL_EXISTS ( "Account with this email already exists "),
    USER_NOT_SAVED ("User was not saved "),
    PHONE_NUMBER_NOT_UNIQUE ("Account with this phone number already exists"),
    EMPTY_FIELD ("All fields should not be empty "),
    USER_NOT_FOUND_BY_EMAIL ("User was not found by email "),

    ACCESS_DENIED("You do not have permission on this operation"),
    PRINCIPAL_NOT_FOUND("Not found principal for this object"),
    PERMISSION_NOT_FOUND("Could not find such permission."),
    USER_DO_NOT_EXISTS("Sorry such user don`t exists (");

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
