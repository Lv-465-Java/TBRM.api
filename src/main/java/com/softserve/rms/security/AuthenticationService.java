package com.softserve.rms.security;

import com.softserve.rms.dto.JwtClaimsDto;
import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.dto.LoginPerson;
import com.softserve.rms.entities.Person;
import com.softserve.rms.exceptions.BadCredentialException;
import com.softserve.rms.exceptions.Message;
import com.softserve.rms.service.impl.PersonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Kravets Maryana
 */
@Component
public class AuthenticationService implements Message {

    private static final Logger LOGGER= LoggerFactory.getLogger(AuthenticationService.class);
    private TokenManagementService tokenManagementService;
    private PersonServiceImpl personService;

    /**
     * constructor
     * @param tokenManagementService {@link TokenManagementService}
     * @param personService {@link PersonServiceImpl}
     */
    public AuthenticationService(@Autowired TokenManagementService tokenManagementService,
                                 @Autowired PersonServiceImpl personService){
        this.tokenManagementService=tokenManagementService;
        this.personService=personService;
    }

    /**
     * authentication user. Method verify user credential. If user is in DB than gererate access and refresh token, if no thrown
     * BadCredentialException exception
     * @param loginPerson {@link LoginPerson}
     * @return JwtDto
     * @throws BadCredentialException
     */
    public JwtDto loginUser(LoginPerson loginPerson){
        LOGGER.info("user login info - {}", loginPerson);

        Person person=personService.getUserByEmail(loginPerson.getEmail());

        if (personService.passwordEncoder().matches(loginPerson.getPassword(),person.getPassword())){
            JwtClaimsDto jwtClaimsDto=new JwtClaimsDto(person.getId());

            return tokenManagementService.generateTokenPair(jwtClaimsDto);

        } else {
            throw new BadCredentialException(BAD_CREDENTIAL_EXCEPTION);
        }
    }

}
