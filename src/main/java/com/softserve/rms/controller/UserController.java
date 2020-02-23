package com.softserve.rms.controller;


import com.softserve.rms.Validator.Trimmer;
import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.user.EmailEditDto;
import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.UserEditDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.security.UserPrincipal;
import com.softserve.rms.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;


@RestController
@Validated
public class UserController {
    private UserService userService;
    private Trimmer trimmer;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public UserController(UserService userService, Trimmer trimmer) {
        this.userService = userService;
        this.trimmer = trimmer;
    }

    /**
     * Update {@link User}.
     *
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PutMapping("/profile/update")
    public ResponseEntity updateUser(@Valid @RequestBody UserEditDto userEditDto,
                                     @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {

        userService.update(trimmer.trimEditData(userEditDto), principal.getUsername());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Update user's password.
     *
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PatchMapping("/profile/update/password")
    public ResponseEntity updatePassword(@Valid @RequestBody PasswordEditDto passwordEditDto,
                                         @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        userService.editPassword(passwordEditDto, principal.getUsername());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Update user's email.
     *
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PatchMapping("/profile/update/email")
    public ResponseEntity updateEmail(@Valid @RequestBody EmailEditDto emailEditDto,
                                      @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        userService.editEmail(emailEditDto, principal.getUsername());
        //TODO redirect to login page
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Delete account
     *
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping("{id}/delete")
    public ResponseEntity deleteAccount(@PathVariable long id){
        userService.deleteAccount(id);
        //TODO redirect to main page
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
