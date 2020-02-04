package com.softserve.rms.service.impl;

import com.softserve.rms.constant.ErrorMessage;
import com.softserve.rms.constant.ValidationErrorConstants;
import com.softserve.rms.dto.PasswordEditDto;
import com.softserve.rms.dto.RegistrationDto;
import com.softserve.rms.dto.UserEditDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.exception.InvalidUserDataException;
import com.softserve.rms.exception.NotSavedException;
import com.softserve.rms.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.powermock.reflect.exceptions.MethodNotFoundException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserValidationServiceImpl .class)
public class UserValidationServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserValidationServiceImpl userValidationService;

    private RegistrationDto user =
            RegistrationDto.builder()
                    .firstName("test")
                    .lastName("test")
                    .email("test@gmail.com")
                    .phone("+380111111111")
                    .password("qwertQWE!@1")
                    .build();
    private RegistrationDto userToTrim =
            RegistrationDto.builder()
                    .firstName("test  ")
                    .lastName("test  ")
                    .email("TeST@gmail.com")
                    .phone("+380111111111  ")
                    .password("qwertQWE!@1")
                    .build();
    private UserEditDto user1 =
             new UserEditDto("test","test","test@gmail.com",
                    "+380111111111");
    private UserEditDto user1ToTrim =
            new UserEditDto("test  "," test","TEST@gmail.com",
                    "  +380111111111 ");
    private PasswordEditDto user2 =
            new PasswordEditDto("qwertQWE!@1");

    @Test
    public void validate() throws Exception {
        UserValidationServiceImpl spy = PowerMockito.spy(userValidationService);
        Map<String,String> map=new HashMap<>();
        PowerMockito.doReturn(user).when(spy, "trimRegistrationData",user);
        PowerMockito.doReturn(map).when(spy, "passwordDataValidation",
                user.getPassword());
        PowerMockito.doReturn(map).when(spy, "basicDataValidation",
                user.getFirstName(), user.getLastName(),user.getEmail(),user.getPhone());
        Assert.assertEquals(user, spy.validate(user));
        PowerMockito.verifyPrivate(spy, Mockito.times(1))
                .invoke("trimRegistrationData",user);
        PowerMockito.verifyPrivate(spy, Mockito.times(1))
                .invoke("passwordDataValidation",
                        user.getPassword());
        PowerMockito.verifyPrivate(spy, Mockito.times(1))
                .invoke("basicDataValidation",
                        user.getFirstName(), user.getLastName(),user.getEmail(),user.getPhone());
    }

    @Test(expected = InvalidUserDataException.class)
    public void validateFail() throws Exception {
        UserValidationServiceImpl spy = PowerMockito.spy(userValidationService);
        Map<String,String> map=new HashMap<>();
        PowerMockito.doReturn(user).when(spy, "trimRegistrationData",user);
        PowerMockito.doReturn(map)
                .when(spy, "passwordDataValidation",
                user.getPassword());
        map.put("invalidFirstName", ValidationErrorConstants.INVALID_FIRSTNAME);
        PowerMockito.doReturn(map)
                .when(spy, "basicDataValidation",
                user.getFirstName(), user.getLastName(),user.getEmail(),user.getPhone());
         spy.validate(user);
        PowerMockito.verifyPrivate(spy, Mockito.times(1))
                .invoke("trimRegistrationData",user);
        PowerMockito.verifyPrivate(spy, Mockito.times(1))
                .invoke("passwordDataValidation",
                        user.getPassword());
        PowerMockito.verifyPrivate(spy, Mockito.times(1))
                .invoke("basicDataValidation",
                        user.getFirstName(), user.getLastName(),user.getEmail(),user.getPhone());
    }

    @Test
    public void validateUpdateData() throws Exception {
        UserValidationServiceImpl spy = PowerMockito.spy(userValidationService);
        Map<String,String> map=new HashMap<>();
        PowerMockito.doReturn(user1).when(spy, "trimEditData",user1);
        PowerMockito.doReturn(map).when(spy, "basicDataValidation",
                user1.getFirstName(), user1.getLastName(),user1.getEmail(),user1.getPhone());
        Assert.assertEquals(user1, spy.validateUpdateData(user1));
        PowerMockito.verifyPrivate(spy, Mockito.times(1))
                .invoke("trimEditData",user1);
        PowerMockito.verifyPrivate(spy, Mockito.times(1))
                .invoke("basicDataValidation",
                        user1.getFirstName(), user1.getLastName(),user1.getEmail(),user1.getPhone());
    }

    @Test(expected = InvalidUserDataException.class)
    public void validateUpdateDataFail() throws Exception {
        UserValidationServiceImpl spy = PowerMockito.spy(userValidationService);
        Map<String,String> map=new HashMap<>();
        PowerMockito.doReturn(user1).when(spy, "trimEditData",user1);
        map.put("invalidFirstName", ValidationErrorConstants.INVALID_FIRSTNAME);
        PowerMockito.doReturn(map)
                .when(spy, "basicDataValidation",
                        user1.getFirstName(), user1.getLastName(),user1.getEmail(),user1.getPhone());
        spy.validateUpdateData(user1);
        PowerMockito.verifyPrivate(spy, Mockito.times(1))
                .invoke("trimEditData",user1);
        PowerMockito.verifyPrivate(spy, Mockito.times(1))
                .invoke("basicDataValidation",
                        user1.getFirstName(), user1.getLastName(),user1.getEmail(),user1.getPhone());
    }


    @Test
    public void validatePassword() throws Exception {
        UserValidationServiceImpl spy = PowerMockito.spy(userValidationService);
        Map<String,String> map=new HashMap<>();
        PowerMockito.doReturn(map).when(spy, "passwordDataValidation",
                user2.getPassword());
        spy.validatePassword(user2);
        verify(spy,times(1)).validatePassword(user2);
        PowerMockito.verifyPrivate(spy, Mockito.times(1))
                .invoke("passwordDataValidation",
                        user2.getPassword());
    }

    @Test(expected = InvalidUserDataException.class)
    public void validatePasswordFail() throws Exception {
        UserValidationServiceImpl spy = PowerMockito.spy(userValidationService);
        Map<String,String> map=new HashMap<>();
        map.put("passwordsDoNotMatches", ErrorMessage.PASSWORDS_NOT_MATCHES);
        PowerMockito.doReturn(map)
                .when(spy, "passwordDataValidation",
                        user2.getPassword());
        spy.validatePassword(user2);
        PowerMockito.verifyPrivate(spy, Mockito.times(1))
                .invoke("passwordDataValidation",
                        user2.getPassword());
    }

    @Test
    public void trimRegistrationData() throws Exception {
        RegistrationDto actual= Whitebox.invokeMethod(userValidationService,
                "trimRegistrationData", userToTrim);
        assertThat(actual, is(user));
    }

    @Test(expected = NullPointerException.class)
    public void trimRegistrationDataFail() throws Exception {
        RegistrationDto actual= Whitebox.invokeMethod(userValidationService,
                "trimRegistrationData", null);
        assertThat(actual, is(user));
    }

    @Test
    public void trimEditData() throws Exception {
        UserEditDto actual= Whitebox.invokeMethod(userValidationService,
                "trimEditData", user1ToTrim);
        assertThat(actual, is(user1));
    }

    @Test(expected = NullPointerException.class)
    public void trimEditDataFail() throws Exception {
        UserEditDto actual= Whitebox.invokeMethod(userValidationService,
                "trimEditData", null);
        assertThat(actual, is(user1));
    }

    @Test
    public void validateByPattern() throws Exception {
        Boolean actual= Whitebox.invokeMethod(userValidationService,
                "validateByPattern", "^\\+[0-9]{12}$","+380738298201");
        assertThat(actual, is(true));
        Boolean secondActual= Whitebox.invokeMethod(userValidationService,
                "validateByPattern", "^\\+[0-9]{12}$","38073828201");
        assertThat(secondActual, is(false));
    }

    @Test(expected = NullPointerException.class)
    public void validateByPatternFail() throws Exception {
        Boolean actual= Whitebox.invokeMethod(userValidationService,
                "validateByPattern", "^\\+[0-9]{12}$",null);
        assertThat(actual, is(true));
    }

    @Test
    public void isBlank() throws Exception {
        Boolean actual= Whitebox.invokeMethod(userValidationService,
                "isBlank", "dscfdsf","dcsdvc  ","dscv");
        assertThat(actual, is(false));
        Boolean secondActual= Whitebox.invokeMethod(userValidationService,
                "isBlank", "dscfdsf","  ","dscv");
        assertThat(secondActual, is(true));
    }

    @Test(expected= MethodNotFoundException.class)
    public void isBlankFail() throws Exception {
        Boolean actual= Whitebox.invokeMethod(userValidationService,
                "isBlank", 1,"dcsdvc  ","dscv");
        assertThat(actual, is(false));

    }

    @Test
    public void passwordDataValidation() throws Exception {
        Map<String,String > actual= Whitebox.invokeMethod(userValidationService,
                "passwordDataValidation", "qwertQWE111!");
        assertThat(actual, is(new HashMap<>()));
        Map<String,String > secondActual= Whitebox.invokeMethod(userValidationService,
                "passwordDataValidation", "qwer1!");
        Map<String,String > actualToCheck=new HashMap<>();
        actualToCheck.put("invalidPassword", ValidationErrorConstants.INVALID_PASSWORD);
        assertThat(secondActual, is(actualToCheck));
    }

    }
