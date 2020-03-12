package com.softserve.rms.security.service;

import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.dto.LoginUser;
import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.BadCredentialException;
import com.softserve.rms.security.AuthenticationService;
import com.softserve.rms.security.TokenManagementService;
import com.softserve.rms.security.config.WebSecurityConfig;
import com.softserve.rms.service.UserService;
import com.softserve.rms.service.implementation.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(value = MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

    @Mock
    UserServiceImpl userService;

    @Mock
    TokenManagementService tokenManagementService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    WebSecurityConfig webSecurityConfig;

    @InjectMocks
    AuthenticationService authenticationService;//= PowerMockito.spy(new AuthenticationService(tokenManagementService,userService,webSecurityConfig));

    User testUser = new User(1L, "first1", "last1", "email1","phone1","$2a$10$AZ.bCtbIMyESrOd/08jZjeC0ffwrm2ThW6BGfs8hZY43d8xn6yz2m",true, new Role(1L,"USER"),"imageUrl","google","324253674", Collections.emptyList(),"token",Collections.emptyList());
    User testUser2 = new User(2L, "first1", "last1", "email1","phone1","password1",false, new Role(1L,"USER"),"imageUrl","google","324253674", Collections.emptyList(),"token",Collections.emptyList());
    LoginUser loginUser = new LoginUser("email1","aaa");

    @Test
    public void loadUserTest(){
        when(userService.getUserByEmail("email1")).thenReturn(testUser);
        when(webSecurityConfig.passwordEncoder()).thenReturn(new BCryptPasswordEncoder());
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(Boolean.TRUE);
        JwtDto jwtDto=authenticationService.loginUser(loginUser);
        assertEquals(anyString(), jwtDto.getAccessToken());
        assertEquals(anyString(), jwtDto.getRefreshToken());

        verify(userService, times(1)).getUserByEmail("email1");

    }

    @Test(expected = BadCredentialException.class)
    public void loadUserFailedTest(){
        when(userService.getUserByEmail("email1")).thenReturn(testUser);
        when(webSecurityConfig.passwordEncoder()).thenReturn(new BCryptPasswordEncoder());
        when(passwordEncoder.matches(anyString(),anyString())).thenReturn(Boolean.FALSE);
        authenticationService.loginUser(loginUser);
    }
}
