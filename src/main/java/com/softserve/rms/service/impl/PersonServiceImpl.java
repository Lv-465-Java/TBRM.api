package com.softserve.rms.service.impl;

import com.softserve.rms.constant.ErrorMessage;
import com.softserve.rms.dto.RegistrationDto;
import com.softserve.rms.dto.UserEditDto;
import com.softserve.rms.entities.Person;
import com.softserve.rms.exception.NotSavedException;
import com.softserve.rms.exception.WrongEmailException;
import com.softserve.rms.repository.PersonRepository;
import com.softserve.rms.service.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Optional;
import java.util.stream.Collectors;


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
        this.personRepository = personRepository;
    }
    //@Autowired
//    private PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    @Override
    public boolean save(RegistrationDto registrationDto) {
        Person person = modelMapper.map(registrationDto, Person.class);
        //person.setPassword(passwordEncoder.encode(person.getPassword()));
        if (personRepository.save(person) == null) {
            new NotSavedException(ErrorMessage.USER_NOT_SAVED);
            return false;
        } else
            return true;
    }

    @Override
    //@Transactional
    public void update(UserEditDto userEditDto, String currentUserEmail){
//       Person person = Optional.of(personRepository.findByEmail(currentUserEmail))
//               .orElseThrow(()->new WrongEmailException
//                       (ErrorMessage.USER_NOT_FOUND_BY_EMAIL+ currentUserEmail));
        Person person=personRepository.findByEmail(currentUserEmail);
        if(person==null){
            throw new WrongEmailException(ErrorMessage.USER_NOT_FOUND_BY_EMAIL+ currentUserEmail);
        }
       person.setFirstName(userEditDto.getFirstName());
       person.setLastName(userEditDto.getLastName());
       person.setEmail(userEditDto.getEmail());
       person.setPhone(userEditDto.getPhone());
       personRepository.save(person);
    }
}