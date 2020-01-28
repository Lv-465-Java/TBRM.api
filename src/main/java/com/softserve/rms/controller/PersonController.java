package com.softserve.rms.controller;


import com.softserve.rms.dto.PersonDto;
import com.softserve.rms.service.PersonService;
import com.softserve.rms.service.PersonValidationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    @Autowired
    private PersonValidationService validateService;

    @PostMapping("/registration")
    public ResponseEntity createPerson(@Valid @RequestBody PersonDto personDto) {
        String error = validateService.validate(personDto);
        if (error == null)
        {
            personService.save(personDto);
            return new ResponseEntity(org.springframework.http.HttpStatus.CREATED);

        } else return new ResponseEntity(error, org.springframework.http.HttpStatus.FORBIDDEN);
    }
}
