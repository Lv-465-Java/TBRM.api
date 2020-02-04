package com.softserve.rms.controller;

import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.dto.LoginUser;
import com.softserve.rms.security.AuthenticationService;
import com.softserve.rms.security.TokenManagementService;
import com.softserve.rms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * Controller that provides authentication and refresh token logic.
 *
 * @author Kravets Maryana
 */
@RestController
public class LoginController {

    private UserService userService;
    private AuthenticationService authenticationService;
    private TokenManagementService tokenManagementService;
    private String AUTHORIZATION_HEADER="Authorization";
    private String REFRESH_HEADER="RefreshToken";
    private String AUTH_HEADER_PREFIX="Bearer ";

    /**
     * Constructor.
     *
     * @param userService {@link UserService}
     * @param authenticationService {@link AuthenticationService}
     * @param tokenManagementService {@link TokenManagementService}
     */
    @Autowired
    public LoginController(UserService userService,
                           AuthenticationService authenticationService,
                           TokenManagementService tokenManagementService){
        this.userService = userService;
        this.authenticationService=authenticationService;
        this.tokenManagementService=tokenManagementService;
    }

    /**
     * Method for user authentication.
     *
     * @param loginUser - {@link LoginUser} that have email and password.
     * @return {@link ResponseEntity}
     */
    @PostMapping("/authentication")
    public ResponseEntity<?> login(@RequestBody @Valid LoginUser loginUser, HttpServletResponse response){

        JwtDto jwtDto=authenticationService.loginUser(loginUser);
        response.setHeader(AUTHORIZATION_HEADER, AUTH_HEADER_PREFIX+ jwtDto.getAccessToken());
        response.setHeader(REFRESH_HEADER, jwtDto.getRefreshToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Method for user authentication.
     *
     * @return {@link ResponseEntity}
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {

        String refresh=tokenManagementService.resolveRefreshToken(request);
        JwtDto newToken = tokenManagementService.refreshTokens(refresh);
        response.setHeader(AUTHORIZATION_HEADER, AUTH_HEADER_PREFIX + newToken.getAccessToken());
        response.setHeader(REFRESH_HEADER, newToken.getRefreshToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
