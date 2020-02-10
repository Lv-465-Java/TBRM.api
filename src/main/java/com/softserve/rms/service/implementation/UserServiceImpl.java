package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.RegistrationDto;
import com.softserve.rms.dto.user.UserEditDto;
import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.Message;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotSavedException;
import com.softserve.rms.exceptions.user.WrongEmailException;
import com.softserve.rms.exceptions.user.WrongPasswordException;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService, Message {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper = new ModelMapper();

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public void update(UserEditDto userEditDto, String currentUserEmail) {
        User user = userRepository.findUserByEmail(currentUserEmail)
                .orElseThrow(() -> new WrongEmailException(ErrorMessage.USER_NOT_FOUND_BY_EMAIL.getMessage() + currentUserEmail));
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
        User user = userRepository.findUserByEmail(currentUserEmail)
                .orElseThrow(() -> new WrongEmailException(ErrorMessage.USER_NOT_FOUND_BY_EMAIL.getMessage() + currentUserEmail));
        if (!passwordEncoder.matches(passwordEditDto.getOldPassword(), user.getPassword())) {
            throw new WrongPasswordException(ErrorMessage.WRONG_PASSWORD.getMessage());
        }
        user.setPassword(passwordEncoder.encode(passwordEditDto.getNewPassword()));
        userRepository.save(user);
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

}