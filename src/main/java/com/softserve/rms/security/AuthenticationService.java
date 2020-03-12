package com.softserve.rms.security;

import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.dto.LoginUser;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.BadCredentialException;
import com.softserve.rms.exceptions.Message;
import com.softserve.rms.security.config.WebSecurityConfig;
import com.softserve.rms.service.implementation.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Class that provides authentication logic.
 *
 * @author Kravets Maryana
 */
@Component
public class AuthenticationService implements Message {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private TokenManagementService tokenManagementService;
    private UserServiceImpl userService;
    private WebSecurityConfig webSecurityConfig;

    /**
     * constructor
     *
     * @param tokenManagementService {@link TokenManagementService}
     * @param userService            {@link UserServiceImpl}
     */
    @Autowired
    public AuthenticationService(TokenManagementService tokenManagementService,
                                 UserServiceImpl userService,
                                 WebSecurityConfig webSecurityConfig) {
        this.tokenManagementService = tokenManagementService;
        this.userService = userService;
        this.webSecurityConfig = webSecurityConfig;
    }

    /**
     * authentication user. Method verify user credential. If user is in DB than generating access and refresh token, if no thrown
     * BadCredentialException exception
     *
     * @param loginUser {@link LoginUser}
     * @return JwtDto
     * @throws BadCredentialException
     */
    public JwtDto loginUser(LoginUser loginUser) {
        LOGGER.info("user login info - {}", loginUser);

        User user = userService.getUserByEmail(loginUser.getEmail());

       PasswordEncoder passwordEncoder=webSecurityConfig.passwordEncoder();

        if (passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            if (!user.isEnabled()) {
                throw new DisabledException(NON_ACTIVE_ACCOUNT_EXCEPTION);
            }
            return tokenManagementService.generateTokenPair(loginUser.getEmail());

        } else {
            throw new BadCredentialException(BAD_CREDENTIAL_EXCEPTION);
        }
    }

}
