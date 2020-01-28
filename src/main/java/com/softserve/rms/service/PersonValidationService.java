package com.softserve.rms.service;

import com.softserve.rms.constant.ErrorMessage;
import com.softserve.rms.constant.ValidationConstants;
import com.softserve.rms.dto.PersonDto;
import com.softserve.rms.entities.Person;
import com.softserve.rms.exception.EmailExistException;
import com.softserve.rms.exception.EmptyFieldException;
import com.softserve.rms.exception.InvalidUserRegistrationDataException;
import com.softserve.rms.exception.PasswordsDoNotMatchesException;
import com.softserve.rms.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class PersonValidationService {
    @Autowired
    private PersonRepository personRepository;
    private Matcher matcher;
    private Pattern pattern;

    public boolean validate(PersonDto person) {
        Map<String, String> map = new HashMap<>();
        if(!passwordMatches(person.getPassword(),person.getPasswordConfirm())){
            map.put("passwordsDoNotMatches",ErrorMessage.PASSWORD_NOT_MATCHES);
        }
      if (!validateName(person.getFirstName())){
            map.put("invalidFirstname",ValidationConstants.INVALID_FIRSTNAME);
        }
      if (!validateName(person.getLastName())){
            map.put("invalidLastname" ,ValidationConstants.INVALID_LASTNAME);
        }
      if (!validateEmail(person.getEmail())){
            map.put("invalidEmail", ValidationConstants.INVALID_EMAIL);
        }
      if(emailExist(person.getEmail())) {
            map.put("emailExist", ErrorMessage.USER_WITH_EMAIL_EXIST);
        }
      if (!validatePhoneNumber(person.getPhone())){
            map.put("invalidPhone", ValidationConstants.INVALID_PHONE);
        }
      if ( !validatePassword(person.getPassword())) {
            map.put("invalidPassword", ValidationConstants.INVALID_PASSWORD);
        }
        if (map.isEmpty()) {
            return true;
        } else {
            throw new InvalidUserRegistrationDataException(map);
        }
    }



    private boolean passwordMatches(String password, String passwordConfirm){
        if(password.equals(passwordConfirm)){
            return true;
        }else return false;
    }

    public boolean emailExist(String email) {
        List<Person> people = personRepository.findByEmail(email);
        if (!people.isEmpty()){
            return true;
        } else return false;
    }

    private boolean validateEmail(String email) {
        pattern = Pattern.compile(
                "^[a-zA-Z0-9]+((\\.|_|-)?[a-zA-Z0-9])+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$");
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validatePhoneNumber(String phone) {
        return phone.matches("^\\+[0-9]{12}$");
    }

    private boolean validatePassword(String password){
        pattern = Pattern.compile(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~`!@#$*%^&(-_)/><?\"|+=:])[A-Za-z\\d~`!@#*$%^&(-_)/><?\"|+=:]{8,}$");
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean validateName(String name ) {
        return name.matches( "^([A-Za-z]+((-|')[A-Za-z]+)*){2,}$" );
    }
}
