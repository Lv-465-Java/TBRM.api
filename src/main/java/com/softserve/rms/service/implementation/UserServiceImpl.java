package com.softserve.rms.service.implementation;

import com.softserve.rms.entities.User;
import com.softserve.rms.entities.Role;
import com.softserve.rms.exceptions.Message;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * The class provides implementation of the {@code UserService}.
 * @author Kravets Maryana
 */
@Service
public class UserServiceImpl implements UserService, Message{

    private final UserRepository userRepository;

    /**
     * constructor
     * @param userRepository {@link UserRepository}
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    /**
     * Method that allow you to get {@link User} by email.
     *
     * @param email a value of {@link String}
     * @return {@link User}
     */
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()-> new NotFoundException(String.format(USER_EMAIL_NOT_FOUND_EXCEPTION,email)));
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
                .orElseThrow(()->new NotFoundException(String.format(USER_NOT_FOUND_EXCEPTION, id)));
    }

    /**
     * BCrypt encoder
     * @return PasswordEncoder
     */
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
