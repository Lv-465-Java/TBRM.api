package com.softserve.rms.service.impl;

import com.softserve.rms.dto.PersonDto;
import com.softserve.rms.entities.Person;
import com.softserve.rms.entities.Role;
import com.softserve.rms.exceptions.Message;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.PersonRepository;
import com.softserve.rms.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PersonServiceImpl implements PersonService, Message{//, UserDetailsService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(@Autowired PersonRepository personRepository){
        this.personRepository=personRepository;
    }

    @Override
    public Person save(PersonDto personDto) {
        Person person=new Person();
        person.setFirstName(personDto.getFirstName());
        person.setLastName(personDto.getLastName());
        person.setEmail(personDto.getEmail());
        person.setPassword(passwordEncoder().encode(personDto.getPassword()));
        person.setPhone(personDto.getPhone());
        person.setRole(new Role(1L,"GUEST"));
       // person.setStatus(Boolean.TRUE);
        person.setId(new Random().nextLong());
        return personRepository.save(person);
    }

    @Override
    public Person getUserByEmail(String email) {
        return personRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException(String.format(USER_EMAIL_NOT_FOUND_EXCEPTION,email)));
    }

    @Override
    public Person getById(long id) {
        return personRepository.findById(id)
                .orElseThrow(()->new NotFoundException(String.format(USER_NOT_FOUND_EXCEPTION, id)));
    }

//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        Person person =personRepository.findByEmail(s)
//                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_EXCEPTION,s)));
//        return new org.springframework.security.core.userdetails.User(person.getEmail(), person.getPassword(), person.isEnabled(),
//                true, true, true, person.getAuthorities());
//    }

    /**
     * BCrypt encoder
     * @return PasswordEncoder
     */
    //@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
