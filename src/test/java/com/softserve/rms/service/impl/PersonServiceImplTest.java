package com.softserve.rms.service.impl;

import com.softserve.rms.dto.RegistrationDto;
import com.softserve.rms.entities.Person;
import com.softserve.rms.repository.PersonRepository;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;
import org.powermock.core.classloader.annotations.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Optional;
//
//@RunWith(PowerMockRunner.class)
//public class PersonServiceImplTest {
//
//    @Mock
//    private PersonRepository personRepository;
//    @Mock
//    private RegistrationDto registrationDto;
//    @Mock
//    private ModelMapper modelMapper;
//    @InjectMocks
//    private PersonServiceImpl personService;
//
//    private Person person =
//            Person.builder()
//                    .id(1L)
//                    .firstName("test")
//                    .lastName("test")
//                    .email("test@gmail.com")
//                    .phone("+380111111111")
//                    .build();
//
//    @Test
//    public void saveTest(){
//        when(personRepository.findByEmail(person.getEmail())).thenReturn(Optional.of(person));
//        when(personRepository.findByEmail(person.getEmail())).thenReturn(Optional.empty());
//        when(personRepository.save(person)).thenReturn(person);
//        registrationDto=ModelMapper.map(person,RegistrationDto.class);
//        assertEquals(person, personService.save(registrationDto));
//    }
//}
