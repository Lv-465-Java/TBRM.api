package com.softserve.rms.service;

import com.softserve.rms.constant.ErrorMessage;
import com.softserve.rms.constant.ValidationConstants;
import com.softserve.rms.dto.RegistrationDto;
import com.softserve.rms.entities.Person;
import com.softserve.rms.exception.InvalidUserRegistrationDataException;
import com.softserve.rms.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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
    public PersonValidationService(PersonRepository personRepository){
        this.personRepository=personRepository;
    }

    /**
     * Method that check if all field in RegistrationDto are valid
     *
     * @param person value of {@link RegistrationDto}
     * @author Mariia Shchur
     */
    public boolean validate(RegistrationDto person) {
        Map<String, String> map = new HashMap<>();
        if(isBlank(person)){
            map.put("emptyField",ErrorMessage.EMPTY_FIELD);
        }
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

    /**
     * Method that check if any field in RegistrationDto is blank.
     *
     * @param person value of {@link RegistrationDto}
     * @author Mariia Shchur
     */
    private boolean isBlank(RegistrationDto person){
        return Stream.of(person.getFirstName(),
                person.getLastName(),
                person.getEmail(),
                person.getPhone(),
                person.getPassword(),
                person.getPasswordConfirm()).anyMatch(x -> x.trim().isEmpty());
    }

    /**
     * Method that check if entered password and passwordConfirm are the same
     *
     * @param password, passwordConfirm
     * @author Mariia Shchur
     */
    private boolean passwordMatches(String password, String passwordConfirm){
        if(password.equals(passwordConfirm)){
            return true;
        }else return false;
    }

    /**
     * Method that check if user with this email already exist
     *
     * @param email
     * @author Mariia Shchur
     */
    public boolean emailExist(String email) {
        List<Person> people = personRepository.findByEmail(email);
        if (!people.isEmpty()){
            return true;
        } else return false;
    }

    /**
     * Method that validate email
     *
     * @param email
     * @author Mariia Shchur
     */
    private boolean validateEmail(String email) {
        pattern = Pattern.compile(
                "^[a-zA-Z0-9]+((\\.|_|-)?[a-zA-Z0-9])+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}$");
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Method that validate phone number
     *
     * @param phone
     * @author Mariia Shchur
     */
    private boolean validatePhoneNumber(String phone) {
        return phone.matches("^\\+[0-9]{12}$");
    }

    /**
     * Method that validate password
     *
     * @param password
     * @author Mariia Shchur
     */
    private boolean validatePassword(String password){
        pattern = Pattern.compile(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~`!@#$*%^&(-_)/><?\"|+=:])[A-Za-z\\d~`!@#*$%^&(-_)/><?\"|+=:]{8,}$");
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * Method that validate name
     *
     * @param name
     * @author Mariia Shchur
     */
    private boolean validateName(String name ) {
        return name.matches( "^([A-Za-z]+((-|')[A-Za-z]+)*){2,}$" );
    }
}
