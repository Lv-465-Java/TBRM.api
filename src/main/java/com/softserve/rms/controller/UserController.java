package com.softserve.rms.controller;


import com.softserve.rms.dto.UserEditDto;
import com.softserve.rms.entities.Person;
import com.softserve.rms.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;

@RestController

public class UserController {
    private PersonService personService;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public UserController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Update {@link Person}.
     *
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @RequestMapping("/user")
    public ResponseEntity updateUser(@Valid @RequestBody UserEditDto userEditDto,String principal){
                                     //@ApiIgnore @AuthenticationPrincipal Principal principal) {

        personService.update(userEditDto,principal);
                //.getName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
