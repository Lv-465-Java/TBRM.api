package com.softserve.rms.security;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * This class is custom implementation of UserDetailsService.
 *
 * @author Artur Sydor
 */
@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    /**
     * Constructor with parameters.
     *
     * @param userRepository object of type UserRepository
     */
    public UserPrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Method retrieves user from DataBase
     * and wrap it into UserPrincipals.
     *
     * @param username represents user`s email
     * @return object of type UserPrincipals, that implement UserDetails interface
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByEmail(username).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_DO_NOT_EXISTS.getMessage()));
        return UserPrincipal.create(user);
    }
}
