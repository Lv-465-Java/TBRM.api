package com.softserve.rms.controller;


import com.softserve.rms.Validator.Trimmer;
import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.user.RegistrationDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@Validated
@RestController
public class RegistrationController {
    private UserService userService;
    private UserValidationServiceImpl validateService;
    private Trimmer trimmer;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public RegistrationController(UserService userService,
                                  UserValidationServiceImpl validateService,
                                  Trimmer trimmer) {
        this.userService = userService;
        this.validateService = validateService;
        this.trimmer = trimmer;
    }

    /**
     * Method that saves {@link User}.
     *
     * @param registrationDto {@link RegistrationDto}
     * @return ResponseEntity
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping("/registration")
    public ResponseEntity<String> createUser(@Valid @RequestBody RegistrationDto registrationDto) {
        userService.save(trimmer.trimRegistrationData(registrationDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

