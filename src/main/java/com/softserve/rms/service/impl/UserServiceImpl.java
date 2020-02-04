package com.softserve.rms.service.impl;

import com.softserve.rms.constant.ErrorMessage;
import com.softserve.rms.dto.PasswordEditDto;
import com.softserve.rms.dto.RegistrationDto;
import com.softserve.rms.dto.UserEditDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.exception.NotSavedException;
import com.softserve.rms.exception.WrongEmailException;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    //private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper = new ModelMapper();

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository){
                           //PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        //this.passwordEncoder=passwordEncoder;
    }


    /**
     * {@inheritDoc }
     *
     * @author Mariia Shchur
     */
    @Override
    public void save(RegistrationDto registrationDto) {
        User user = modelMapper.map(registrationDto, User.class);
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        //user=userRepository.save(user);
        if (userRepository.save(user)==null) {
            new NotSavedException(ErrorMessage.USER_NOT_SAVED);
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
        User user = userRepository.findByEmail(currentUserEmail);
        if (user == null) {
            throw new WrongEmailException(ErrorMessage.USER_NOT_FOUND_BY_EMAIL + currentUserEmail);
        }
        user.setFirstName(userEditDto.getFirstName());
        user.setLastName(userEditDto.getLastName());
        user.setEmail(userEditDto.getEmail());
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
        User user = userRepository.findByEmail(currentUserEmail);
        if (user == null) {
            throw new WrongEmailException(ErrorMessage.USER_NOT_FOUND_BY_EMAIL + currentUserEmail);
        }
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword(passwordEditDto.getPassword());
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}