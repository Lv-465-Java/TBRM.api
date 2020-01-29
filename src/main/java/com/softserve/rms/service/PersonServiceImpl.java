package com.softserve.rms.service;

import com.softserve.rms.constant.ErrorMessage;
import com.softserve.rms.dto.PersonDto;
import com.softserve.rms.entities.Person;
import com.softserve.rms.exception.NotSavedException;
import com.softserve.rms.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository=personRepository;
    }
    //@Autowired
//    private PasswordEncoder passwordEncoder;


    public boolean save(PersonDto personDto) {
        Person person = modelMapper.map(personDto,Person.class);
        //person.setPassword(passwordEncoder.encode(person.getPassword()));
        if(personRepository.save(person)==null){
            new NotSavedException(ErrorMessage.USER_NOT_SAVED);
            return false;
        } else
            return true;
    }
}
