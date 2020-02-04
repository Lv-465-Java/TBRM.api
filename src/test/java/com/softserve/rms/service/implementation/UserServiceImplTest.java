package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.RegistrationDto;
import com.softserve.rms.dto.user.UserEditDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotSavedException;
import com.softserve.rms.exceptions.user.WrongEmailException;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.implementation.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.powermock.modules.junit4.PowerMockRunner;


import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;


@RunWith(PowerMockRunner.class)
//@SpringBootTest
//@PrepareForTest({UserRepository.class})
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
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

    @Test
    public void saveTest() {
        when(userRepository.save(any())).thenReturn(user);
        userService.save(new RegistrationDto());
        verify(userRepository,times(1)).save(any());
        assertEquals(user, userRepository.save(user));
    }

    @Test(expected = NotSavedException.class)
    public void saveWithErrorTest() {
        when(userRepository.save(any())).thenThrow(NotSavedException.class);
        when(modelMapper.map(any(), any())).thenReturn(null);
        userService.save(new RegistrationDto());
    }

    @Test
    public void updateTest(){
        when(userRepository.findByEmail(any())).thenReturn(user);
        when(userRepository.save(any())).thenReturn(user);
        userService.update(new UserEditDto(),user.getEmail());
        verify(userRepository,times(1)).save(any());
    }

    @Test(expected = WrongEmailException.class)
    public void updateWrongEmailTest(){
        when(userRepository.findByEmail(any())).thenReturn(null);
        userService.update(new UserEditDto(),any());
    }

    @Test
    public void editPasswordTest(){
        when(userRepository.save(any())).thenReturn(user);
        when(userRepository.findByEmail(any())).thenReturn(user);
        userService.editPassword(new PasswordEditDto(),
                user.getEmail());
        verify(userRepository,times(1)).save(any());
    }

    @Test(expected = WrongEmailException.class)
    public void editPasswordWrongEmailTest(){
        when(userRepository.findByEmail(any())).thenReturn(null);
        userService.editPassword(new PasswordEditDto(),any());
    }

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
