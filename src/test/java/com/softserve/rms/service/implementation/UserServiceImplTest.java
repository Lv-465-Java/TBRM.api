package com.softserve.rms.service.implementation;

import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.implementation.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    User testUser = new User(1L, "first1", "last1", "email1","phone1","password1",true, new Role(1L,"USER"), Collections.emptyList());

    @Test
    public void getUserByEmailTest(){

        when(userRepository.findByEmail("email1")).thenReturn(Optional.of(testUser));

        User expectedUser = userService.getUserByEmail("email1");

        assertEquals(testUser, expectedUser);

        verify(userRepository, times(1)).findByEmail("email1");
    }

    @Test(expected = NotFoundException.class)
    public void getByEmailExceptionTest() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        userService.getUserByEmail(anyString());
    }

    @Test
    public void getUserByIdTest() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User expectedUser = userService.getById(1L);

        assertEquals(testUser, expectedUser);

        verify(userRepository, times(1)).findById(1L);
    }

    @Test(expected = NotFoundException.class)
    public void getByIdExceptionTest() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        userService.getById(anyLong());
    }
}
