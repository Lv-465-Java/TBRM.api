package com.softserve.rms.controller;


import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.UserEditDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.service.UserService;
import com.softserve.rms.service.implementation.UserValidationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private UserValidationServiceImpl validateService;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public UserController(UserService userService, UserValidationServiceImpl validateService) {
        this.userService = userService;
        this.validateService=validateService;
    }

    /**
     * Update {@link User}.
     *
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @PutMapping
    public ResponseEntity updateUser( @RequestBody UserEditDto userEditDto,String principal){
                                     //@ApiIgnore @AuthenticationPrincipal Principal principal) {

        userService.update(validateService.validateUpdateData(userEditDto),principal);
                //.getUserName());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Update user's password.
     *
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @PatchMapping
    public ResponseEntity updatePassword( @RequestBody PasswordEditDto passwordEditDto, String principal) {
        //@ApiIgnore @AuthenticationPrincipal Principal principal) {)
        validateService.validatePassword(passwordEditDto);
        userService.editPassword(passwordEditDto,principal);
        //.getUserName());
         return ResponseEntity.status(HttpStatus.OK).build();
    }
}
