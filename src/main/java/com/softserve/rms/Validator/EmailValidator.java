package com.softserve.rms.Validator;

import com.softserve.rms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailExist, String> {

    private final UserRepository userRepository;
    @Autowired
    public EmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return userRepository.existsUserByEmail(email) ? false : true;
    }
}
