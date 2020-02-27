package com.softserve.rms.controller;

import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.constants.ValidationErrorConstants;
import com.softserve.rms.constants.ValidationPattern;
import com.softserve.rms.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@RestController
@Validated
public class ResetPasswordController {

    private UserService userService;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public ResetPasswordController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Method that send link for password
     * resetting on entered email address
     *
     * @param email
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/forgot_password")
    public ResponseEntity forgotPassword(@Valid @NotBlank(message = ValidationErrorConstants.EMPTY_EMAIL)
                                          @Email(message = ValidationErrorConstants.INVALID_EMAIL)
                                          @RequestParam("email") String email) {
        userService.sendLinkForPasswordResetting(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method for password resetting
     *
     * @param token
     * @param password
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping("/reset_password")
    public ResponseEntity resetPassword(@RequestParam("token") String token,
                                        @Valid @NotEmpty(message = ValidationErrorConstants.EMPTY_PASSWORD)
                                        @Pattern(regexp = ValidationPattern.PASSWORD_PATTERN,
                                                message = ValidationErrorConstants.INVALID_PASSWORD)
                                        @RequestBody String password) {
        userService.resetPassword(token, password);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

