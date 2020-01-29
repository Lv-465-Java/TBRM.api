package com.softserve.rms.controller;

import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.dto.LoginPerson;
import com.softserve.rms.dto.PersonDto;
import com.softserve.rms.security.AuthenticationService;
import com.softserve.rms.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class LoginController {

    private PersonService personService;
    private AuthenticationService authenticationService;

    public LoginController(@Autowired PersonService personService,
                           @Autowired AuthenticationService authenticationService){
        this.personService=personService;
        this.authenticationService=authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid PersonDto personDto){
        return new ResponseEntity<>(personService.save(personDto), HttpStatus.OK);
    }

    @PostMapping("/authentication")
    public ResponseEntity<?> login(@RequestBody @Valid LoginPerson loginPerson, HttpServletResponse response){

        JwtDto jwtDto=authenticationService.loginUser(loginPerson);
        response.setHeader("Authorization", jwtDto.getAccessToken());
        response.setHeader("RefreshToken", jwtDto.getRefreshToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
