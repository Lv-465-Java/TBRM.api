package com.softserve.rms.security.service;

import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.dto.LoginUser;
import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.BadCredentialException;
import com.softserve.rms.security.AuthenticationService;
import com.softserve.rms.security.TokenManagementService;
import com.softserve.rms.security.config.WebSecurityConfig;
import com.softserve.rms.service.implementation.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    WebSecurityConfig webSecurityConfig;

    @InjectMocks
    AuthenticationService authenticationService;

    User testUser = new User(1L, "first1", "last1", "email1","phone1","$2a$10$AZ.bCtbIMyESrOd/08jZjeC0ffwrm2ThW6BGfs8hZY43d8xn6yz2m",true, new Role(1L,"USER"),"imageUrl","google","324253674", Collections.emptyList(),"token",Collections.emptyList());
    User testUser1 = new User(1L, "first1", "last1", "email1","phone1","$2a$10$AZ.bCtbIMyESrOd/08jZjeC0ffwrm2ThW6BGfs8hZY43d8xn6yz2m",true, new Role(1L,"USER"),"imageUrl","google","324253674", Collections.emptyList(),"token",Collections.emptyList());
    User testUser2 = new User(2L, "first1", "last1", "email2","phone1","$2a$10$AZ.bCtbIMyESrOd/08jZjeC0ffwrm2ThW6BGfs8hZY43d8xn6yz2m",false, new Role(1L,"USER"),"imageUrl","google","324253674", Collections.emptyList(),"token",Collections.emptyList());
    LoginUser loginUser = new LoginUser("email1","aaa");
    LoginUser loginUser1 = new LoginUser("email1","aaaa");
    LoginUser loginUser2 = new LoginUser("email2","aaa");
    JwtDto jwtDto=new JwtDto("accessToken", "refreshToken");

    @Test
    public void loadUserTest(){
        when(userService.getUserByEmail(loginUser.getEmail())).thenReturn(testUser);
        when(webSecurityConfig.passwordEncoder()).thenReturn(new BCryptPasswordEncoder());
        when(tokenManagementService.generateTokenPair(loginUser.getEmail())).thenReturn(jwtDto);
        jwtDto=authenticationService.loginUser(loginUser);
        assertEquals("accessToken", jwtDto.getAccessToken());
        assertEquals("refreshToken", jwtDto.getRefreshToken());

        verify(userService, times(1)).getUserByEmail("email1");

    }

    @Test(expected = BadCredentialException.class)
    public void loadUserFailedTest(){
        when(userService.getUserByEmail(loginUser1.getEmail())).thenReturn(testUser1);
        when(webSecurityConfig.passwordEncoder()).thenReturn(new BCryptPasswordEncoder());
        authenticationService.loginUser(loginUser1);
    }

    @Test(expected = DisabledException.class)
    public void loadNonActiveUserTest(){
        when(userService.getUserByEmail(loginUser2.getEmail())).thenReturn(testUser2);
        when(webSecurityConfig.passwordEncoder()).thenReturn(new BCryptPasswordEncoder());
        authenticationService.loginUser(loginUser2);
    }
}
