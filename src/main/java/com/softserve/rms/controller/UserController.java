package com.softserve.rms.controller;


import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.UserEditDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.security.UserPrincipal;
import com.softserve.rms.service.UserService;
import com.softserve.rms.service.implementation.UserValidationServiceImpl;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;



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
        this.validateService = validateService;
    }

    /**
     * Update {@link User}.
     *
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 403,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @PutMapping
    public ResponseEntity updateUser(@RequestBody UserEditDto userEditDto,
                                     @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {

        userService.update(validateService.validateUpdateData(userEditDto), principal.getUsername());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Update user's password.
     *
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 403,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @PatchMapping
    public ResponseEntity updatePassword(@RequestBody PasswordEditDto passwordEditDto,
        @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        validateService.validatePassword(passwordEditDto);
        userService.editPassword(passwordEditDto, principal.getUsername());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
