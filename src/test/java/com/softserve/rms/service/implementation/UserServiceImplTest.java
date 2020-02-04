package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.RegistrationDto;
import com.softserve.rms.dto.user.UserEditDto;
import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotSavedException;
import com.softserve.rms.exceptions.user.WrongEmailException;
import com.softserve.rms.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Collections;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    User testUser = new User(1L, "first1", "last1", "email1","phone1","password1",true, new Role(1L,"USER"), Collections.emptyList());

    private User user =
            User.builder()
                    .id(1L)
                    .firstName("test")
                    .lastName("test")
                    .email("test@gmail.com")
                    .phone("+380111111111")
                    .password("qwertQWE!@1")
                    .build();

    private User secondUser =
            User.builder()
                    .id(1L)
                    .firstName("test2")
                    .lastName("test2")
                    .email("test2@gmail.com")
                    .phone("+380222222222")
                    .password("qwertQWE!@2")
                    .build();

    private RegistrationDto registrationDto =
            RegistrationDto.builder()
                    .firstName("test")
                    .lastName("test")
                    .email("test@gmail.com")
                    .phone("+380111111111")
                    .password("qwertQWE!@1")
                    .build();
    private UserEditDto userEditDto =
            new UserEditDto("test","test","test@gmail.com",
                    "+380111111111");

    private PasswordEditDto passwordEditDto =
            new PasswordEditDto("qwertQWE!@1");

    @Test
    public void saveTest() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.save(registrationDto);
        verify(userRepository,times(1)).save(any());
        assertEquals(user, userRepository.save(user));
    }

    @Test(expected = NotSavedException.class)
    public void saveWithErrorTest() {
        when(userRepository.save(any())).thenReturn(null);
        userService.save(registrationDto);
    }

    @Test
    public void updateTest(){
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        userService.update(userEditDto,user.getEmail());
        verify(userRepository,times(1)).save(any());
    }

    @Test(expected = WrongEmailException.class)
    public void updateWrongEmailTest(){
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());
        userService.update(userEditDto,any());
        verify(userRepository,times(1)).save(any());
    }

    @Test
    public void editPasswordTest(){
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(user));
        userService.editPassword(passwordEditDto,
                user.getEmail());
        verify(userRepository,times(1)).save(any());
    }

    @Test(expected = WrongEmailException.class)
    public void editPasswordWrongEmailTest(){
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.empty());
        userService.editPassword(passwordEditDto,any());
        verify(userRepository,times(1)).save(any());
    }

    @Test
    public void getUserByEmailTest(){

        when(userRepository.findUserByEmail("email1")).thenReturn(Optional.of(testUser));

        User expectedUser = userService.getUserByEmail("email1");

        assertEquals(testUser, expectedUser);

        verify(userRepository, times(1)).findUserByEmail("email1");
    }

    @Test(expected = NotFoundException.class)
    public void getByEmailExceptionTest() {

        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

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
