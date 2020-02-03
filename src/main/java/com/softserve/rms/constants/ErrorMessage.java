package com.softserve.rms.constants;

public enum ErrorMessage {

    ACCESS_DENIED("You do not have permission on this operation"),
    PRINCIPAL_NOT_FOUND("Not found principal for this object");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
