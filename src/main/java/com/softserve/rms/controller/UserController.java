package com.softserve.rms.controller;

import com.softserve.rms.dto.UserSearchDTO;
import com.softserve.rms.dto.user.*;
import com.softserve.rms.validator.Trimmer;
import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.UserPasswordPhoneDto;
import com.softserve.rms.dto.UserDto;
import com.softserve.rms.dto.UserPasswordPhoneDto;
import com.softserve.rms.dto.UserDtoRole;
import com.softserve.rms.dto.user.EmailEditDto;
import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.PermissionUserDto;
import com.softserve.rms.dto.user.UserEditDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.security.TokenManagementService;
import com.softserve.rms.security.UserPrincipal;
import com.softserve.rms.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@RestController
@Validated
public class UserController {
    private UserService userService;
    private Trimmer trimmer;
    private TokenManagementService tokenManagementService;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public UserController(UserService userService, Trimmer trimmer, TokenManagementService tokenManagementService) {
        this.userService = userService;
        this.trimmer = trimmer;
        this.tokenManagementService = tokenManagementService;
    }

    /**
     * Method that return user's data.
     *
     * @return {@link UserDto}.
     * @author Mariia Shchur
     */
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfileData(
            @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(principal.getUsername()));
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
     * Method for updating profile picture.
     *
     * @param file to save.
     * @return url of the updated file.
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PutMapping("/profile/updatePhoto")
    public ResponseEntity changePhoto(@RequestPart(value = "file") MultipartFile file,
                                      @ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        userService.changePhoto(file, principal.getUsername());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method for deleting profile picture
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping("/profile/deletePhoto")
    public ResponseEntity deletePhoto(@ApiIgnore @AuthenticationPrincipal UserPrincipal principal) {
        userService.deletePhoto(principal.getUsername());
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
    @DeleteMapping("/delete")
    public ResponseEntity deleteAccount(@ApiIgnore
                                        @AuthenticationPrincipal UserPrincipal principal) {
        userService.deleteAccount(principal.getUsername());
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/user/role")
    public ResponseEntity<UserDtoRole> getUserRole(Principal principal) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserRole(principal));
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/user")
    public ResponseEntity<Page<PermissionUserDto>> getUsers(@RequestParam Optional<Integer> page,
                                                            @RequestParam Optional<Integer> pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5)));
    }

    /**
     * Set password and phone of user for full oauth authentication
     *
     * @param accessToken          {@link String}
     * @param userPasswordPhoneDto {@link UserPasswordPhoneDto}
     * @param response             {@link HttpServletResponse}
     * @return httpStatus {@link HttpStatus}
     * @author Kravets Maryana
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping("/oauth2/fullRegister")
    public ResponseEntity<Object> getFullAuthenticate(@RequestHeader(name = "authorization") String accessToken, @RequestBody UserPasswordPhoneDto userPasswordPhoneDto, HttpServletResponse response) {

        String email = tokenManagementService.getUserEmail(accessToken.substring(7));
        userService.setPasswordAndPhone(email, userPasswordPhoneDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Get users by role
     *
     * @param role     {@link String}
     * @param page     {@link Optional<Integer>}
     * @param pageSize {@link Optional<Integer>}
     * @return page of users {@link Page<UserDto>}
     * @author Kravets Maryana
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/users/role")
    public ResponseEntity<Page<UserSearchDTO>> getByRole(@RequestParam String role,
                                                         @RequestParam Optional<Integer> page,
                                                         @RequestParam Optional<Integer> pageSize) {
        Page<UserSearchDTO> users = userService.getUsersByRole(role, page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}