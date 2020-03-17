package com.softserve.rms.constants;

public interface ValidationPattern {
    String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~`!@#$*%^&(-_)/><?\"|+=:])[A-Za-z\\d~`!@#*$%^&(-_)/><?\"|+=:]{8,}$";
    String EMAIL_PATTERN = "^\\s*[a-zA-Z0-9]+((\\.|_|-)?[a-zA-Z0-9])+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}\\s*$";
    String PHONE_PATTERN = "^\\s*\\+[0-9]{12}\\s*$";
    String NAME_PATTERN = "^\\s*([A-Za-z]+((-|')[A-Za-z]+)*){2,}\\s*$";
    String SEARCH_PATTERN = "(\\w+?)(:|<|>|=|!=)([A-Za-z0-9~`!@#*$%^&(-_)/><?\"|+=: ]+)";
}