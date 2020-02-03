package com.softserve.rms.security;

import com.softserve.rms.entities.User;
import com.softserve.rms.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findPersonByEmail(username).orElseThrow(() -> new RuntimeException("No user found"));
        return new UserPrincipal(user);
    }
}