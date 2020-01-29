package com.softserve.rms.constant;

public class ValidationPattern {

    public static final String PASSWORD_PATTERN= "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~`!@#$*%^&(-_)/><?\"|+=:])[A-Za-z\\d~`!@#*$%^&(-_)/><?\"|+=:]{8,}$";
    public static final String EMAIL_PATTERN="^[a-zA-Z0-9]+((\\.|_|-)?[a-zA-Z0-9])+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$";
    public static final String PHONE_PATTERN="^\\+[0-9]{12}$";
    public static final String NAME_PATTERN="^([A-Za-z]+((-|')[A-Za-z]+)*){2,}$";

}
