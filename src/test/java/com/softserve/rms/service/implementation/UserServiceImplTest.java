package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.UserDto;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.dto.user.*;
import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.InvalidTokenException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotSavedException;
import com.softserve.rms.exceptions.user.PhoneExistException;
import com.softserve.rms.exceptions.user.WrongEmailException;
import com.softserve.rms.exceptions.user.WrongPasswordException;
import com.softserve.rms.repository.AdminRepository;
import com.softserve.rms.repository.UserHistoryRepository;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.implementation.UserServiceImpl;
import com.sun.security.auth.UserPrincipal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.security.Principal;
import javax.sql.DataSource;
import java.util.*;

import static com.softserve.rms.exceptions.Message.USER_EMAIL_NOT_FOUND_EXCEPTION;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.everythingDeclaredIn;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserServiceImpl.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Principal principal;
    @Mock
    private UserServiceImpl userService2;
    @Mock
    private JavaMailSender javaMailSender;
    @Mock
    private UserHistoryRepository userHistoryRepository;
    @Mock
    private FileStorageServiceImpl fileStorageService;
    @Mock
    private AdminRepository adminRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void initializeMock() {
        userService = PowerMockito.spy(new UserServiceImpl(userRepository,
                adminRepository,userHistoryRepository,passwordEncoder, fileStorageService,javaMailSender,null));
    }

    private Principal principal2 = new UserPrincipal("test3@gmail.com");

    User testUser = new User(1L, "first1", "last1", "email1","phone1","password1",true, new Role(1L,"USER"),"imageUrl","google","324253674", Collections.emptyList(),"token",Collections.emptyList());

    private User user =
            User.builder()
                    .id(1L)
                    .firstName("test")
                    .lastName("test")
                    .imageUrl("image")
                    .email("test@gmail.com")
                    .phone("+380111111111")
                    .password("qwertQWE!@1")
                    .build();

    private UserDto userDtoFirst =
            UserDto.builder()
                    .id(1L)
                    .firstName("test")
                    .lastName("test")
                    .imageUrl("nullimage")
                    .email("test@gmail.com")
                    .phone("+380111111111")
                    .password("qwertQWE!@1")
                    .build();

    private User thirdUser =
            User.builder()
                    .id(1L)
                    .firstName("test3")
                    .lastName("test3")
                    .email("test3@gmail.com")
                    .phone("+380333333333")
                    .password("mwertQWE!@2")
                    .role(new Role(1L, "ROLE_MANAGER"))
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
            new UserEditDto(Optional.of("test"),Optional.of("test"),
                    Optional.of("+380111111111"));

    private PasswordEditDto passwordEditDto =
            new PasswordEditDto("qwertQWE!@1","qwertyQQ1!!");

    private PermissionUserDto permissionUserDto =
            new PermissionUserDto("test3@gmail.com", "test3", "test3", new Role(1L, "ROLE_MANAGER"));

    @Test
    public void saveSuccess() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.save(registrationDto);
        verify(userRepository,times(1)).save(any());
        assertEquals(user, userRepository.save(user));
    }

    @Test(expected = NotSavedException.class)
    public void saveFail() {
        when(userRepository.save(any())).thenReturn(null);
        userService.save(registrationDto);
    }

    @Test
    public void updateSuccess(){
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.existsUserByPhone(user.getPhone())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        userService.update(userEditDto,user.getEmail());
        verify(userRepository,times(1)).save(user);
    }

    @Test
    public void editPasswordSuccess() {
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(),any())).thenReturn(true);
        userService.editPassword(passwordEditDto,
                user.getEmail());
        verify(userRepository,times(1)).save(any());
    }

    @Test(expected = WrongPasswordException.class)
    public void editPasswordWrongPasswordFail() {
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(),any())).thenReturn(false);
        userService.editPassword(passwordEditDto,
                user.getEmail());
        verify(userRepository,times(1)).save(any());
    }

    @Test
    public void getUserSuccess(){
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(user));
        UserDto userDto = userService.getUser("test@gmail.com");
        assertEquals(userDto,userDtoFirst);

    }

    @Test
    public void sendLinkForPasswordResettingSuccess() throws Exception{
        PowerMockito.doNothing().when(userService,"sendMail",Mockito.any(SimpleMailMessage.class));
        when(userRepository.findUserByEmail(any())).thenReturn(Optional.of(user));
        userService.sendLinkForPasswordResetting(user.getEmail());
        verify(userRepository,times (1)).save(user);
    }


    @Test
    public void resetPasswordSuccess() throws Exception {
        when(userRepository.findUserByResetToken(anyString())).thenReturn(Optional.of(user));
        PowerMockito.doReturn(true).when(userService,"checkTokenDate",anyString());
        userService.resetPassword(anyString(),anyString());
        verify(userRepository,times(1)).save(user);
    }

    @Test(expected = InvalidTokenException.class)
    public void resetPasswordFail() throws Exception {
        when(userRepository.findUserByResetToken(anyString())).thenReturn(Optional.of(user));
        PowerMockito.doReturn(false).when(userService,"checkTokenDate",anyString());
        userService.resetPassword(anyString(),anyString());
    }

    @Test
    public void sendMailSuccess() throws Exception {
        Mockito.doNothing().when(javaMailSender).send(Mockito.any(SimpleMailMessage.class));
        verifyPrivate(userService, times(0)).
                invoke("sendMail", Mockito.any(SimpleMailMessage.class));
    }

    @Test
    public void checkTokenDateSuccess() throws Exception {
        when(userHistoryRepository.checkTokenDate(anyString())).thenReturn(new HashMap<String, Object>());
        verifyPrivate(userService, times(0)).
                invoke("checkTokenDate", anyString());
    }

    @Test
    public void deletePhotoSuccess(){
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
        doNothing().when(fileStorageService).deleteFile("" );
        when(userRepository.save(user)).thenReturn(user);
        userService.deletePhoto("");
        verify(userRepository,times(1)).save(user);
    }

    @Test
    public void deleteAccountSuccess(){
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(user.getEmail());
        doNothing().when(fileStorageService).deleteFile("");
        doNothing().when(userRepository).deleteByEmail(anyString());
        userService.deleteAccount(user.getEmail());
        verify(userRepository,times(1)).deleteByEmail(user.getEmail());
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

    @Test
    public void getUserRoleTestSuccess() {
        lenient().when(userService2.getUserByEmail(anyString())).thenReturn(thirdUser);
        userService2.getUserRole(principal2);
    }

    @Test(expected = NotFoundException.class)
    public void getUserRoleTestFail() {
        doThrow(new NotFoundException("exception")).when(userRepository).findUserByEmail(anyString());
        userService.getUserByEmail(anyString());
    }

    @Test
    public void getUsersTestSuccess() {
        Page<User> userPage = new PageImpl<>(Collections.singletonList(thirdUser));
        Mockito.when(adminRepository.getAllByEnabled(anyBoolean(), any(Pageable.class))).thenReturn(userPage);
        List<PermissionUserDto> resourceTemplateDTOs = Collections.singletonList(permissionUserDto);
        Assert.assertEquals(resourceTemplateDTOs, userService.getUsers(1, 1).getContent());
    }
}
