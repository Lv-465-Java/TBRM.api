package com.softserve.rms.constants;

public enum ErrorMessage {
    CAN_NOT_FIND_A_RESOURCE_TEMPLATE("There is not such a resource template");

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
