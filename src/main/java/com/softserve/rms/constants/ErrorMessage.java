package com.softserve.rms.constants;

public enum ErrorMessage {
    DENIED_ACCESS("Only users, who have role MANAGER can delete permission.");

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
