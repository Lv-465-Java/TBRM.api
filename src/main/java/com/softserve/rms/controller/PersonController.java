package com.softserve.rms.controller;


import com.softserve.rms.dto.PersonDto;
import com.softserve.rms.service.PersonService;
import com.softserve.rms.service.PersonValidationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PersonController {
    private PersonService personService;
    private PersonValidationService validateService;

    @Autowired
    public PersonController(PersonService personService,
                            PersonValidationService validateService){
        this.personService=personService;
        this.validateService=validateService;
    }

    @PostMapping("/registration")
    public HttpStatus createPerson(@Valid @RequestBody PersonDto personDto) {
        validateService.validate(personDto);
        personService.save(personDto);
        return HttpStatus.CREATED;
    }
}

