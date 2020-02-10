package com.softserve.rms.Validator;

import com.softserve.rms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<PhoneExist, String> {

    private final UserRepository userRepository;
    @Autowired
    public PhoneValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
       return  userRepository.existsUserByPhone(phone) ? false : true;
    }
}