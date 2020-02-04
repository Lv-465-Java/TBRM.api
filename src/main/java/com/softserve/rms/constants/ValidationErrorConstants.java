package com.softserve.rms.constants;

public enum ValidationErrorConstants {
    INVALID_FIRSTNAME("FirstName field is not valid"),
    INVALID_LASTNAME ("LastName field is not valid"),
    INVALID_EMAIL( "Email is invalid"),
    INVALID_PHONE ("Phone number is invalid"),
    INVALID_PASSWORD ("Password must contain at least eight characters and at least one character of "
            + " uppercase letter, lowercase letter, digit, special character");

    private String message;
    ValidationErrorConstants(String message) {
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
