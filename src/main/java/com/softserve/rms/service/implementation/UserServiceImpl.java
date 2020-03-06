package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.UserDto;
import com.softserve.rms.dto.UserPasswordPhoneDto;
import com.softserve.rms.dto.user.EmailEditDto;
import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.RegistrationDto;
import com.softserve.rms.dto.user.UserEditDto;
import com.softserve.rms.dto.UserDtoRole;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.dto.user.*;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.InvalidTokenException;
import com.softserve.rms.exceptions.NotDeletedException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotSavedException;
import com.softserve.rms.exceptions.PermissionException;
import com.softserve.rms.exceptions.user.WrongEmailException;
import com.softserve.rms.exceptions.user.WrongPasswordException;
import com.softserve.rms.repository.AdminRepository;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import java.util.Map;
import java.util.UUID;

import static com.softserve.rms.exceptions.Message.USER_EMAIL_NOT_FOUND_EXCEPTION;
import static com.softserve.rms.exceptions.Message.USER_NOT_FOUND_EXCEPTION;


@Service
@EnableAutoConfiguration
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;
    private FileStorageServiceImpl fileStorageService;
    private ModelMapper modelMapper = new ModelMapper();
    public final JavaMailSender javaMailSender;
    private final JdbcTemplate jdbcTemplate;
    private String endpointUrl;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AdminRepository adminRepository,
                           PasswordEncoder passwordEncoder,
                           FileStorageServiceImpl fileStorageService,
                           JavaMailSender javaMailSender,
                           DataSource dataSource,
                           @Value("${ENDPOINT_URL}") String endpointUrl) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService=fileStorageService;
        this.javaMailSender = javaMailSender;
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.endpointUrl = endpointUrl;
    }

    /**
     * {@inheritDoc }
     *
     * @author Mariia Shchur
     */
    @Override
    public void changePhoto(MultipartFile multipartFile, String email){
        User user = getUserByEmail(email);
        if(user.getImageUrl()!=null) {
            fileStorageService.deleteFile(user.getImageUrl());
        }
        String photoName=fileStorageService.uploadFile(multipartFile);
        user.setImageUrl(photoName);
        userRepository.save(user);
    }

    /**
     * {@inheritDoc }
     *
     * @author Mariia Shchur
     */
    @Override
    public void deletePhoto(String email){
        User user = getUserByEmail(email);
        fileStorageService.deleteFile(user.getImageUrl());
        user.setImageUrl(null);
        userRepository.save(user);
    }

    /**
     * {@inheritDoc }
     *
     * @author Mariia Shchur
     */
    @Override
    public void save(RegistrationDto registrationDto) {
        Role role = new Role(5L, "ROLE_GUEST");
        User user = modelMapper.map(registrationDto, User.class);
        user.setRole(role);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userRepository.save(user) == null) {
            throw new NotSavedException(ErrorMessage.USER_NOT_SAVED.getMessage());
        }
    }

    /**
     * {@inheritDoc }
     *
     * @author Mariia Shchur
     */
    @Override
    @Transactional


    public void deleteAccount(String email) {
        User user = getUserByEmail(email);
        try {
            if((user.getImageUrl()!=null)&&(user.getProvider()==null)){
                fileStorageService.deleteFile(user.getImageUrl());
            }
            userRepository.deleteByEmail(email);
        }
        catch (NotDeletedException e){
            throw new NotDeletedException(ErrorMessage.USER_NOT_DELETE.getMessage());
        }
    }

    /**
     * {@inheritDoc }
     *
     * @author Mariia Shchur
     */
    @Override
    @Transactional
    public void update(UserEditDto userEditDto, String currentUserEmail) {
        User user = getUserByEmail(currentUserEmail);
        user.setFirstName(userEditDto.getFirstName());
        user.setLastName(userEditDto.getLastName());
        user.setPhone(userEditDto.getPhone());
        userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    @Override
    public void editPassword(PasswordEditDto passwordEditDto, String currentUserEmail) {
        User user = getUserByEmail(currentUserEmail);
        if (!passwordEncoder.matches(passwordEditDto.getOldPassword(), user.getPassword())) {
            throw new WrongPasswordException(ErrorMessage.WRONG_PASSWORD.getMessage());
        }
        user.setPassword(passwordEncoder.encode(passwordEditDto.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    @Override
    public void editEmail(EmailEditDto emailEditDto, String currentUserEmail) {
        User user = getUserByEmail(currentUserEmail);
        if (!passwordEncoder.matches(emailEditDto.getPassword(), user.getPassword())) {
            throw new WrongPasswordException(ErrorMessage.WRONG_PASSWORD.getMessage());
        }
        user.setEmail(emailEditDto.getEmail());
        userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    public UserDto getUser(String email){
        User user = getUserByEmail(email);
        UserDto userDto=modelMapper.map(user,UserDto.class);
        userDto.setImageUrl(getPhotoUrl(user.getImageUrl()));
        return userDto;
    }

    /**
     * Method that return full url of file from s3
     *
     * @param photoName a value of {@link String}
     * @return {@link String}
     * @author Mariia Shchur
     */
    private String getPhotoUrl(String photoName){
        return endpointUrl+photoName;
    }


    /**
     * Method that allow you to get {@link User} by email.
     *
     * @param email a value of {@link String}
     * @return {@link User}
     */
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format(USER_EMAIL_NOT_FOUND_EXCEPTION, email)));
    }

    /**
     * Method that allow you to get {@link User} by ID.
     *
     * @param id a value of {@link Long}
     * @return {@link User}
     */
    @Override
    public User getById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_EXCEPTION, id)));
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    @Override
    @Transactional
    public void sendLinkForPasswordResetting(String email) {
        User user = getUserByEmail(email.trim());
        user.setResetToken(UUID.randomUUID().toString());
        userRepository.save(user);
        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        passwordResetEmail.setTo(email);
        passwordResetEmail.setSubject("Password reset request");
        passwordResetEmail.setText("To complete the password reset process, please click here: "
                + "http://localhost:3000/reset_password?token=" + user.getResetToken());
        sendMail(passwordResetEmail);
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    @Override
    public void resetPassword(String token, String password) {
        User user = userRepository.findUserByResetToken(token)
                .orElseThrow(() -> new InvalidTokenException(ErrorMessage.INVALID_LINK_OR_TOKEN.getMessage()));
        if (checkTokenDate(token)) {
            user.setPassword(passwordEncoder.encode(password));
            user.setResetToken(null);
            userRepository.save(user);
        } else throw new InvalidTokenException(ErrorMessage.TOKEN_EXPIRED.getMessage());
    }

    /**
     * Method that send massage to mail
     *
     * @param mailMessage massage to send
     */
    private void sendMail(SimpleMailMessage mailMessage) {
        javaMailSender.send(mailMessage);
    }

    /**
     * Method that check if token is still valid
     * (token is valid during 6 hours)
     *
     * @param token a value of {@link Long}
     * @return boolean
     */
    private static final String IF_TOKEN_VALID = "select (to_timestamp(r.revtstmp/ 1000)+ interval '6 hour')>=now() from users_aud u,revinfo r where u.rev=r.rev \n" +
            "and u.reset_token=?  ";
    private Boolean checkTokenDate(String token) {
        boolean q = false;
        Map<String, Object> map = jdbcTemplate.queryForMap(IF_TOKEN_VALID, token);
        for (Map.Entry<String, Object> w : map.entrySet()) {
            if (w.getValue().equals(true)) {
                q = true;
            }
        }
        return q;
    }
    /**
     * Method returns user's role
     *
     * @param principal authenticated user
     * @author Marian Dutchyn
     */
    @Override
    public UserDtoRole getUserRole(Principal principal){
        User user = getUserByEmail(principal.getName());
        UserDtoRole userRoleDto = new UserDtoRole();
        userRoleDto.setRole(user.getRole());
        return userRoleDto;
    }
    /**
     * Method returns active users
     *
     * @author Marian Dutchyn
     */
    @Override
    public List<PermissionUserDto> getUsers() {
        List<User> users = adminRepository.getAllByEnabled(true);
        return users.stream()
                .map(user -> modelMapper.map(user, PermissionUserDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Method for set password and phone of user
     *
     * @param email {@link String}
     * @param userPasswordPhoneDto {@link UserPasswordPhoneDto}
     * @author Kravets Maryana
     */
    @Override
    public void setPasswordAndPhone(String email, UserPasswordPhoneDto userPasswordPhoneDto) {
        User user = getUserByEmail(email);
        user.setPassword(passwordEncoder.encode(userPasswordPhoneDto.getPassword()));
        user.setPhone(userPasswordPhoneDto.getPhone());
        userRepository.save(user);
    }
}