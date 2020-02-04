package com.softserve.rms.controller;


import com.softserve.rms.dto.RegistrationDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.service.UserService;
import com.softserve.rms.service.impl.UserValidationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegistrationController {
    private UserService userService;
    private UserValidationServiceImpl validateService;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public RegistrationController(UserService userService,
                                  UserValidationServiceImpl validateService) {
        this.userService = userService;
        this.validateService = validateService;
    }

    /**
     * Method which save {@link User}.
     *
     * @param registrationDto {@link RegistrationDto}
     * @return ResponseEntity
     * @author Mariia Shchur
     */

    @PostMapping("/registration")
    public ResponseEntity createPerson(@Valid @RequestBody RegistrationDto registrationDto) {
        userService.save(validateService.validate(registrationDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

