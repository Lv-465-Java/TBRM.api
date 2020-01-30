package com.softserve.rms.service.impl;

import com.softserve.rms.constant.ErrorMessage;
import com.softserve.rms.constant.ValidationErrorConstants;
import com.softserve.rms.constant.ValidationPattern;
import com.softserve.rms.dto.PasswordEditDto;
import com.softserve.rms.dto.RegistrationDto;
import com.softserve.rms.dto.UserEditDto;
import com.softserve.rms.exception.InvalidUserDataException;
import com.softserve.rms.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class PersonValidationService {
    private final PersonRepository personRepository;
    private Matcher matcher;
    private Pattern pattern;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public PersonValidationService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    //TODO leave three almost similar methods or only one with spaghetti code
    //TODO or create abstract dto class with validate method

    /**
     * Method that check if all field in RegistrationDto are valid
     *
     * @param person value of {@link RegistrationDto}
     * @author Mariia Shchur
     */
    public boolean validate(RegistrationDto person) {
        Map<String, String> map = passwordDataValidation(person.getPassword(),
                person.getPasswordConfirm());
        map.putAll(basicDataValidation(person.getFirstName(),
                person.getLastName(),person.getEmail(),person.getPhone()));
        if (map.isEmpty()) {
            return true;
        } else {
            throw new InvalidUserDataException(map);
        }
    }

    /**
     * Method that check if all field in UserEditDto are valid
     *
     * @param user value of {@link UserEditDto}
     * @author Mariia Shchur
     */
    public boolean validateUpdateData(UserEditDto user){
        Map<String,String> map = basicDataValidation(user.getFirstName(),user.getLastName(),
                user.getEmail(),user.getPhone());
        if (map.isEmpty()) {
            return true;
        } else {
            throw new InvalidUserDataException(map);
        }
    }

    /**
     * Method that check if all field in PasswordEditDto are valid
     *
     * @param passwordEditDto value of {@link PasswordEditDto}
     * @author Mariia Shchur
     */
    public boolean validatePassword(PasswordEditDto passwordEditDto){
        Map<String, String> map = passwordDataValidation(passwordEditDto.getPassword(),
                passwordEditDto.getPasswordConfirm());
        if (map.isEmpty()) {
            return true;
        } else {
            throw new InvalidUserDataException(map);
        }
    }

//    public boolean generalValidate(Object o){
//        Map<String, String> map=new HashMap<>();
//        if (o instanceof PasswordEditDto){
//            PasswordEditDto p=(PasswordEditDto)o;
//            map.putAll(passwordDataValidation(p.getPassword(),
//                    p.getPasswordConfirm()));
//        }
//        if (o instanceof UserEditDto ){
//            UserEditDto p=(UserEditDto)o;
//            map.putAll(basicDataValidation(p.getFirstName(),p.getLastName(),
//                    p.getEmail(),p.getPhone()));
//        }
//        if (o instanceof RegistrationDto){
//            RegistrationDto p=(RegistrationDto)o;
//            map.putAll(basicDataValidation(p.getFirstName(),p.getLastName(),
//                    p.getEmail(),p.getPhone()));
//            map.putAll(passwordDataValidation(p.getPassword(),
//                    p.getPasswordConfirm()));
//        }
//        if (map.isEmpty()) {
//            return true;
//        } else {
//            throw new InvalidUserRegistrationDataException(map);
//        }
//    }

    /**
     * Method that validate user's data
     *
     * @param firstName,lastName,email,phone
     * @author Mariia Shchur
     */
    public Map<String,String> basicDataValidation(String firstName,
                                                  String lastName,
                                                  String email,String phone ){
        Map<String, String> map = new HashMap<>();
        if (isBlank(firstName,lastName,email,phone)) {
            map.put("emptyField", ErrorMessage.EMPTY_FIELD);
        }
        if (!validateByPattern(ValidationPattern.NAME_PATTERN, firstName)) {
            map.put("invalidFirstName", ValidationErrorConstants.INVALID_FIRSTNAME);
        }
        if (!validateByPattern(ValidationPattern.NAME_PATTERN, lastName)) {
            map.put("invalidLastName", ValidationErrorConstants.INVALID_LASTNAME);
        }
        if (!validateByPattern(ValidationPattern.EMAIL_PATTERN, email)) {
            map.put("invalidEmail", ValidationErrorConstants.INVALID_EMAIL);
        }
        if(personRepository.existsPersonByEmail(email)){
            map.put("emailExists", ErrorMessage.USER_WITH_EMAIL_EXISTS);
        }
        if (!validateByPattern(ValidationPattern.PHONE_PATTERN, phone)) {
            map.put("invalidPhone", ValidationErrorConstants.INVALID_PHONE);
        }
        return map;
    }

    /**
     * Method that validate password
     *
     * @param password, passwordConfirm
     * @author Mariia Shchur
     */
    public Map<String,String> passwordDataValidation(String password, String passwordConfirm){
        Map<String, String> map = new HashMap<>();
        if (!passwordMatches(password, passwordConfirm)) {
            map.put("passwordsDoNotMatches", ErrorMessage.PASSWORDS_NOT_MATCHES);
        }
        if (!validateByPattern(ValidationPattern.PASSWORD_PATTERN, password)) {
            map.put("invalidPassword", ValidationErrorConstants.INVALID_PASSWORD);
        }
        return map;
    }
    /**
     * Method that check if any entered field is blank.
     *
     * @param data
     * @author Mariia Shchur
     */
    private boolean isBlank(String... data) {
        return Stream.of(data).anyMatch(x -> x.trim().isEmpty());
    }

    /**
     * Method that check if entered data match entered string pattern
     *
     * @param patternToCheckWith, data
     * @author Mariia Shchur
     */
    private boolean validateByPattern(String patternToCheckWith, String data) {
        pattern = Pattern.compile(patternToCheckWith);
        matcher = pattern.matcher(data);
        return matcher.matches();
    }

    /**
     * Method that check if entered password and passwordConfirm are the same
     *
     * @param password, passwordConfirm
     * @author Mariia Shchur
     */
    private boolean passwordMatches(String password, String passwordConfirm) {
        if (password.equals(passwordConfirm)) {
            return true;
        } else return false;
    }

}
