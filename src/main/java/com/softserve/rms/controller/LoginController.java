package com.softserve.rms.controller;

import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.JwtDto;
import com.softserve.rms.dto.LoginUser;
import com.softserve.rms.security.AuthenticationService;
import com.softserve.rms.security.TokenManagementService;
import com.softserve.rms.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private AuthenticationService authenticationService;
    private TokenManagementService tokenManagementService;
    private String AUTHORIZATION_HEADER="authorization";
    private String REFRESH_HEADER="refreshToken";
    private String AUTH_HEADER_PREFIX="Bearer ";

    /**
     * Constructor.
     *
     * @param authenticationService {@link AuthenticationService}
     * @param tokenManagementService {@link TokenManagementService}
     */
    @Autowired
    public LoginController(AuthenticationService authenticationService,
                           TokenManagementService tokenManagementService){
        this.authenticationService=authenticationService;
        this.tokenManagementService=tokenManagementService;
    }

    /**
     * Method for user authentication.
     *
     * @param loginUser - {@link LoginUser} that have email and password.
     * @return {@link ResponseEntity}
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED)
    })
    @PostMapping("/authentication")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginUser loginUser, HttpServletResponse response){

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
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping("/refresh")
    public ResponseEntity<Object> refresh(@RequestHeader(name = "refreshToken")  String refresh, HttpServletResponse response) {

        JwtDto newToken = tokenManagementService.refreshTokens(refresh);
        response.setHeader(AUTHORIZATION_HEADER, AUTH_HEADER_PREFIX + newToken.getAccessToken());
        response.setHeader(REFRESH_HEADER, newToken.getRefreshToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping("/oauth2/fullRegister")
    public ResponseEntity<Object> fullAuthenticate(@RequestHeader(name = "refreshToken")  String refresh, HttpServletResponse response) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/oauth2/fullRegister")
    public String getFullAuthenticate(@RequestHeader(name = "refreshToken")  String refresh, HttpServletResponse response) {


        return "http://localhost:3000/oauth2/fullRegister";
    }
}
