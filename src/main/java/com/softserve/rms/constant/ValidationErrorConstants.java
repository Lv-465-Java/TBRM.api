package com.softserve.rms.constant;

public interface ValidationErrorConstants {
    String EMPTY_FIRSTNAME = "FirstName field can not be empty";
    String INVALID_FIRSTNAME = "FirstName field is not valid";

    String EMPTY_LASTNAME = "LastName field can not be empty";
    String INVALID_LASTNAME = "LastName field is not valid";

    String EMPTY_EMAIL = "Email field can not be empty";
    String INVALID_EMAIL = "Email is invalid";

    String EMPTY_PHONE = "Phone field can not be empty";
    String INVALID_PHONE = "Phone number is invalid";
    //String EXIST_PHONE = "Account with this phone number already exist";

    String INVALID_PASSWORD = "Password must contain at least eight characters and at least one character of "
            + " uppercase letter, lowercase letter, digit, special character";
}
