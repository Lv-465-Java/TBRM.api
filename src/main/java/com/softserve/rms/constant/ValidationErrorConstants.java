package com.softserve.rms.constant;

public interface ValidationErrorConstants {
    String INVALID_FIRSTNAME = "FirstName field is not valid";
    String INVALID_LASTNAME = "LastName field is not valid";
    String INVALID_EMAIL = "Email is invalid";
    String INVALID_PHONE = "Phone number is invalid";

    String INVALID_PASSWORD = "Password must contain at least eight characters and at least one character of "
            + " uppercase letter, lowercase letter, digit, special character";
}
