package com.softserve.rms.security;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.doThrow;

@RunWith(PowerMockRunner.class)
public class UserPrincipalDetailsServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserPrincipalDetailsService userPrincipalDetailsService;

    private User user;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Role role = new Role();
        role.setName("MANAGER");
        user = new User();
        user.setEmail("user@com");
        user.setPassword("1111");
        user.setEnabled(true);
        user.setRole(role);
    }

    @Test
    public void loadUserByUsernameOk() {
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
        UserDetails expected = new UserPrincipal(user);
        UserDetails actual = userPrincipalDetailsService.loadUserByUsername("user@com");
        assertEquals(expected, actual);
    }

    @Test(expected = NotFoundException.class)
    public void loadUserByUsernameFail() {
        doThrow(new NotFoundException(ErrorMessage.USER_DO_NOT_EXISTS.getMessage()))
                .when(userRepository).findUserByEmail(anyString());
        userPrincipalDetailsService.loadUserByUsername("user@com");
    }
}