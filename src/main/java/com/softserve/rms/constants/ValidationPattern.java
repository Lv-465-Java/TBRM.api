package com.softserve.rms.constants;

public interface ValidationPattern {

    String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~`!@#$*%^&(-_)/><?\"|+=:])[A-Za-z\\d~`!@#*$%^&(-_)/><?\"|+=:]{8,}$";
    String EMAIL_PATTERN ="^[a-zA-Z0-9]+((\\.|_|-)?[a-zA-Z0-9])+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$";
    String PHONE_PATTERN ="^\\+[0-9]{12}$";
    String NAME_PATTERN ="^([A-Za-z]+((-|')[A-Za-z]+)*){2,}$";

}
