package com.softserve.rms.service;

import com.softserve.rms.dto.PersonDto;
import com.softserve.rms.entities.Person;

public interface PersonService {

    Person save(PersonDto user);

    Person getUserByEmail(String email);

    Person getById(long id);
}
