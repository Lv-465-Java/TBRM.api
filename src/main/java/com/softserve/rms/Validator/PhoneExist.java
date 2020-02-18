package com.softserve.rms.Validator;

import com.softserve.rms.constants.ValidationErrorConstants;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
@Documented
public @interface PhoneExist {

    String message() default ValidationErrorConstants.PHONE_NUMBER_NOT_UNIQUE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
