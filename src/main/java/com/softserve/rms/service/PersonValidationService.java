package com.softserve.rms.service;

import com.softserve.rms.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PersonValidationService {
    @Autowired
    private PersonRepository personRepository;
    private Matcher matcher;
    private Pattern pattern;
}
