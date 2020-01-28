package com.softserve.rms.service;

import com.softserve.rms.constant.ErrorMessage;
import com.softserve.rms.constant.ValidationConstants;
import com.softserve.rms.dto.PersonDto;
import com.softserve.rms.entities.Person;
import com.softserve.rms.exception.EmailExistException;
import com.softserve.rms.exception.EmptyFieldException;
import com.softserve.rms.exception.PasswordsDoNotMatchesException;
import com.softserve.rms.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public String validate(PersonDto person) {
      if(!passwordMatches(person.getPassword(),person.getPasswordConfirm())){
            throw new PasswordsDoNotMatchesException(ErrorMessage.PASSWORD_NOT_MATCHES);
        }else if (!validateName(person.getFirstName())){
            return ValidationConstants.INVALID_FIRSTNAME;
        } else if (!validateName(person.getLastName())){
            return ValidationConstants.INVALID_LASTNAME;
        } else if (!validateEmail(person.getEmail())){
            return ValidationConstants.INVALID_EMAIL;
        } else if(emailExist(person.getEmail())) {
            throw new EmailExistException(ErrorMessage.USER_WITH_EMAIL_EXIST);
        }else if (!validatePhoneNumber(person.getPhone())){
            return ValidationConstants.INVALID_PHONE;
        }else if ( !validatePassword(person.getPassword())) {
            return ValidationConstants.INVALID_PASSWORD;
        } else return null;
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
