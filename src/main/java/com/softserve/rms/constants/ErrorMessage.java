package com.softserve.rms.constants;

public enum ErrorMessage {
    DENIED_ACCESS("You don`t have permission on this operation."),
    PERMISSION_NOT_FOUND("Could not find such permission.");

    private String message;

    private ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
