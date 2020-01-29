package com.softserve.rms.service;

import com.softserve.rms.constant.ErrorMessage;
import com.softserve.rms.dto.RegistrationDto;
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

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository=personRepository;
    }
    //@Autowired
//    private PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    public boolean save(RegistrationDto registrationDto) {
        Person person = modelMapper.map(registrationDto,Person.class);
        //person.setPassword(passwordEncoder.encode(person.getPassword()));
        if(personRepository.save(person)==null){
            new NotSavedException(ErrorMessage.USER_NOT_SAVED);
            return false;
        } else
            return true;
    }
}
