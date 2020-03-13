package com.softserve.rms.constants;

public enum ErrorMessage {
    CAN_NOT_FIND_A_RESOURCE_TEMPLATE("There is no such a resource template"),
    RESOURCE_TEMPLATE_IS_ALREADY_PUBLISHED("Resource template is already published"),
    RESOURCE_TEMPLATE_CAN_NOT_HAVE_RELATIONS("You can't add relation to the resource template, " +
            "which has not been published yet"),
    RESOURCE_TEMPLATE_IS_NOT_PUBLISHED("Resource template has not been published"),
    RESOURCE_TEMPLATE_DO_NOT_HAVE_ANY_PARAMETERS("Resource template must consist of at least one parameter"),
    RESOURCE_TEMPLATE_NAME_IS_NOT_UNIQUE("Resource template name should be unique"),
    RESOURCE_TEMPLATE_TABLE_NAME_IS_NOT_UNIQUE("This resource template name is not allowed"),
    RESOURCE_TEMPLATE_CAN_NOT_BE_UPDATED("Resource template is published and can not be updated"),
    RESOURCE_TEMPLATE_CAN_NOT_BE_DELETED("Resource template can not be deleted"),
    RESOURCE_TEMPLATE_TABLE_CAN_NOT_BE_DROPPED("You can not cancel resource template publish, " +
            "since it has related resources"),
    RESOURCE_TEMPLATE_TABLE_CAN_NOT_BE_DELETED("There are references to this resource template table"),
    RESOURCE_TEMPLATE_CAN_NOT_BE_PUBLISHED("You can not publish template, because its parameter references to " +
            "the template, which is not published: "),

    RESOURCE_PARAMETER_CAN_NOT_BE_FOUND_BY_ID("Resource parameter does not exist with id:  "),
    RESOURCE_TEMPLATE_HAS_NOT_ANY_PARAMETERS("There is no parameters for template with id: "),
    RESOURCE_PARAMETER_CAN_NOT_BE_DELETE_BY_ID("Resource parameter is not deleted with id: "),
    RESOURCE_PARAMETER_IS_ALREADY_EXISTED("Resource parameter is already existed with name: "),
    RESOURCE_PARAMETER_NAME_IS_NOT_UNIQUE("Resource template already has a parameter with provided name"),
    RESOURCE_PARAMETER_COLUMN_NAME_IS_NOT_UNIQUE("This resource parameter name is not allowed"),
    RESOURCE_PARAMETER_CAN_NOT_BE_UPDATED("Resource parameter can not be updated since resource template " +
            "has been published"),
    PARAMETER_CAN_NOT_BE_ADDED("Parameter can not be added since resource template has been published"),
    RESOURCE_PARAMETER_CAN_NOT_BE_DELETED("Resource parameter can not be deleted since resource template " +
            "has been published"),

    USER_NOT_SAVED("User was not saved "),
    USER_NOT_DELETE("User was not deleted"),
    USER_NOT_FOUND_BY_EMAIL("User was not found by email "),

    ACCESS_DENIED("You do not have permission on this operation"),
    PRINCIPAL_NOT_FOUND("Not found principal for this object"),
    PERMISSION_NOT_FOUND("Could not find such permission."),
    USER_DO_NOT_EXISTS("Sorry such user don`t exists ("),
    GROUP_DO_NOT_EXISTS("Sorry such group don`t exists ("),
    GROUP_ALREADY_EXIST("Sorry such group already exists (:"),
    GROUP_MEMBER_ALREADY_EXISTS("Sorry,but user that you want to add is member of this group"),
    GROUP_ACCESS("Sorry you are not owner of this group, so yuo cannot manage it ("),
    NOT_UNIQUE_PERMISSION("Permission you try to add already exists"),
    CANNOT_ADD_EMPTY_NAME("Sorry you cannot add group with no name ):"),

    CAN_NOT_FIND_A_RESOURCE_TABLE("There is no such a table: "),
    CAN_NOT_FIND_A_RESOURCE_BY_ID("There is no such a resource with id: "),
    RESOURCE_CAN_NOT_BE_DELETED_BY_ID("Resource can not be deleted with id: "),
    RESOURCE_CAN_NOT_BE_SAVED("Resource can not be saved, wrong data "),

    WRONG_PASSWORD("Wrong password"),
    INVALID_LINK_OR_TOKEN("Link is invalid or broken"),
    TOKEN_EXPIRED("Token is already expired"),

    WRONG_SEARCH_CRITERIA("Search criteria are not valid"),
    INVALID_COLUMN_CRITERIA("Invalid search query"),
    INVALID_SEARCH_CRITERIA("Your search criteria are not valid"),
    INVALID_TABLE_CRITERIA("Table you are searching in doesn't exist");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
