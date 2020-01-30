package com.softserve.rms.controller;


import com.softserve.rms.dto.PasswordEditDto;
import com.softserve.rms.dto.UserEditDto;
import com.softserve.rms.entities.Person;
import com.softserve.rms.service.PersonService;
import com.softserve.rms.service.impl.PersonValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {
    private PersonService personService;
    private PersonValidationService validateService;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public UserController(PersonService personService,PersonValidationService validateService) {
        this.personService = personService;
        this.validateService=validateService;
    }

    /**
     * Update {@link Person}.
     *
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @PutMapping
    public ResponseEntity updateUser(@Valid @RequestBody UserEditDto userEditDto,String principal){
                                     //@ApiIgnore @AuthenticationPrincipal Principal principal) {

        validateService.validateUpdateData(userEditDto);
        personService.update(userEditDto,principal);
                //.getName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping
    public ResponseEntity updatePassword(@Valid @RequestBody PasswordEditDto passwordEditDto, String principal) {
        //@ApiIgnore @AuthenticationPrincipal Principal principal) {)
        validateService.validatePassword(passwordEditDto);
        personService.editPassword(passwordEditDto,principal);
         return ResponseEntity.status(HttpStatus.OK).build();
    }
}
