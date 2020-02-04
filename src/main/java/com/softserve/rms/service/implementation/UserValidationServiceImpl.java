package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ValidationErrorConstants;
import com.softserve.rms.constants.ValidationPattern;
import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.RegistrationDto;
import com.softserve.rms.dto.user.UserEditDto;
import com.softserve.rms.exceptions.user.InvalidUserDataException;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class UserValidationServiceImpl implements UserValidationService {
    private final UserRepository userRepository;
    private Matcher matcher;
    private Pattern pattern;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public UserValidationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public RegistrationDto validate(RegistrationDto user) {
        user=trimRegistrationData(user);
        Map<String, String> map = passwordDataValidation(user.getPassword());
        map.putAll(basicDataValidation(user.getFirstName(),
                user.getLastName(),user.getEmail(),user.getPhone()));
        if (!map.isEmpty()) {
           throw new InvalidUserDataException(map);
        }
        return user;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public UserEditDto validateUpdateData(UserEditDto user){
        user=trimEditData(user);
        Map<String,String> map = basicDataValidation(user.getFirstName(),user.getLastName(),
                user.getEmail(),user.getPhone());
        if (!map.isEmpty()) {
            throw new InvalidUserDataException(map);
        }
        return user;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void validatePassword(PasswordEditDto passwordEditDto){
        Map<String, String> map = passwordDataValidation(passwordEditDto.getPassword());
        if (!map.isEmpty()) {
            throw new InvalidUserDataException(map);
        }
    }

    /**
     * Method that trim registrationDto data and set email to lower case
     *
     * @param registrationDto value of {@link RegistrationDto}
     * @return {@link RegistrationDto}.
     * @author Mariia Shchur
     */
    private RegistrationDto trimRegistrationData(RegistrationDto registrationDto){
        return new RegistrationDto(registrationDto.getFirstName().trim(),
                registrationDto.getLastName().trim(),
                registrationDto.getEmail().toLowerCase().trim(),
                registrationDto.getPhone().trim(),
                registrationDto.getPassword());
    }

    /**
     * Method that trim userEditDto data and set email to lower case
     *
     * @param userEditDto value of {@link UserEditDto}
     * @return {@link UserEditDto}.
     * @author Mariia Shchur
     */
    private UserEditDto trimEditData(UserEditDto userEditDto){
        return new UserEditDto(userEditDto.getFirstName().trim(),
                userEditDto.getLastName().trim(),
                userEditDto.getEmail().toLowerCase().trim(),
                userEditDto.getPhone().trim());
    }


    /**
     * Method that validate basic user's data
     *
     * @param firstName,lastName,email,phone
     * @author Mariia Shchur
     */
    private Map<String,String> basicDataValidation(String firstName,
                                                  String lastName,
                                                  String email,String phone ){
        Map<String, String> map = new HashMap<>();

        if (isBlank(firstName,lastName,email,phone)) {
            map.put("emptyField", ErrorMessage.EMPTY_FIELD.getMessage());
        }
        if (!validateByPattern(ValidationPattern.NAME_PATTERN, firstName)) {
            map.put("invalidFirstName", ValidationErrorConstants.INVALID_FIRSTNAME.getMessage());
        }
        if (!validateByPattern(ValidationPattern.NAME_PATTERN, lastName)) {
            map.put("invalidLastName", ValidationErrorConstants.INVALID_LASTNAME.getMessage());
        }
        if(userRepository.existsUserByEmail(email)){
            map.put("emailExists", ErrorMessage.USER_WITH_EMAIL_EXISTS.getMessage());
        }
        if (!validateByPattern(ValidationPattern.EMAIL_PATTERN, email)) {
            map.put("invalidEmail", ValidationErrorConstants.INVALID_EMAIL.getMessage());
        }
        if (!validateByPattern(ValidationPattern.PHONE_PATTERN, phone)) {
            map.put("invalidPhone", ValidationErrorConstants.INVALID_PHONE.getMessage());
        }
        if(userRepository.existsUserByPhone(phone)){
            map.put("phoneNotUnique",ErrorMessage.PHONE_NUMBER_NOT_UNIQUE.getMessage());
        }
        return map;
    }

    /**
     * Method that validate password
     *
     * @param password, passwordConfirm
     * @author Mariia Shchur
     */
    private Map<String,String> passwordDataValidation(String password){
        Map<String, String> map = new HashMap<>();
        if (!validateByPattern(ValidationPattern.PASSWORD_PATTERN, password)) {
            map.put("invalidPassword", ValidationErrorConstants.INVALID_PASSWORD.getMessage());
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

}
