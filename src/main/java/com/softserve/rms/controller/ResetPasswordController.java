package com.softserve.rms.controller;

import com.softserve.rms.constants.ValidationErrorConstants;
import com.softserve.rms.constants.ValidationPattern;
import com.softserve.rms.service.UserService;
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
    @GetMapping("/forgot_password")
    public ResponseEntity forgotPassword(@Valid @NotBlank(message = ValidationErrorConstants.EMPTY_EMAIL)
                                          @Email(message = ValidationErrorConstants.INVALID_EMAIL)
                                          @RequestParam("email") String email) {
        userService.sendLinkToResetPassword(email);
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

