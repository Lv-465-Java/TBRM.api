package com.softserve.rms.service;

import com.softserve.rms.constant.ErrorMessage;
import com.softserve.rms.dto.PersonDto;
import com.softserve.rms.entities.Person;
import com.softserve.rms.exception.NotSavedException;
import com.softserve.rms.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepository personRepository;
    //@Autowired
//    private PasswordEncoder passwordEncoder;

    private Person convertDtoToPerson(PersonDto personDto){
        return Person.builder().
                firstName(personDto.getFirstName()).
                lastName(personDto.getLastName()).
                email(personDto.getEmail()).
                phone(personDto.getPhone()).
                //password(passwordEncoder.encode(personDto.getPassword())).
                        password(personDto.getPassword()).
                        build();
    }

    public boolean save(PersonDto person) {
        if(personRepository.save(convertDtoToPerson(person))==null){
            new NotSavedException(ErrorMessage.USER_NOT_SAVED);
            return false;
        } else
            return true;
    }
}
