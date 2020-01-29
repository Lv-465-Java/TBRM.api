package com.softserve.rms.controller;


import com.softserve.rms.dto.RegistrationDto;
import com.softserve.rms.entities.Person;
import com.softserve.rms.service.PersonService;
import com.softserve.rms.service.PersonValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PersonController {
    private PersonService personService;
    private PersonValidationService validateService;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public PersonController(PersonService personService,
                            PersonValidationService validateService){
        this.personService=personService;
        this.validateService=validateService;
    }

    /**
     * The method which save {@link Person}.
     *
     * @param registrationDto {@link RegistrationDto}
     * @return HttpStatus code
     * @author Mariia Shchur
     */

    @PostMapping("/registration")
    public HttpStatus createPerson(@Valid @RequestBody RegistrationDto registrationDto) {
        validateService.validate(registrationDto);
        personService.save(registrationDto);
        return HttpStatus.CREATED;
    }
}

