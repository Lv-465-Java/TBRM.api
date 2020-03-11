package com.softserve.rms.service;

import com.softserve.rms.dto.UserPasswordPhoneDto;
import com.softserve.rms.dto.user.EmailEditDto;
import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.RegistrationDto;
import com.softserve.rms.dto.user.UserEditDto;
import com.softserve.rms.dto.UserDto;
import com.softserve.rms.dto.UserPasswordPhoneDto;
import com.softserve.rms.dto.user.EmailEditDto;
import com.softserve.rms.dto.user.PasswordEditDto;
import com.softserve.rms.dto.user.RegistrationDto;
import com.softserve.rms.dto.user.UserEditDto;
import com.softserve.rms.dto.UserDtoRole;
import com.softserve.rms.dto.user.*;
import com.softserve.rms.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Service
public interface UserService {

    /**
     * Method that return user data
     *
     * @param email
     * @return {@link UserDto}
     * @author Mariia Shchur
     */
    UserDto getUser(String email);

    /**
     * Method saves {@link User}.
     *
     * @param person {@link RegistrationDto}
     * @return boolean
     * @author Mariia Shchur
     */
    void save(RegistrationDto person);

    /**
     * Method update {@link User}.
     *
     * @param userEditDto {@link UserEditDto},currentUserEmail
     * @author Mariia Shchur
     */
    void update(UserEditDto userEditDto, String currentUserEmail);

    /**
     * Method update user's password.
     *
     * @param passwordEditDto {@link PasswordEditDto},currentUserEmail
     * @author Mariia Shchur
     */
    void editPassword(PasswordEditDto passwordEditDto, String currentUserEmail);

    /**
     * Method update user's email.
     *
     * @param emailEditDto {@link EmailEditDto},currentUserEmail
     * @author Mariia Shchur
     */
    void editEmail(EmailEditDto emailEditDto, String currentUserEmail);

    /**
     * Method that allow you to change profile picture
     *
     * @param multipartFile photo for saving.
     * @param email of current user
     * @author Mariia Shchur
     */
    void changePhoto(MultipartFile multipartFile, String email);

    /**
     * Method that allow you to delete profile picture
     *
     * @param email of current user
     * @author Mariia Shchur
     */
    void deletePhoto(String email);

    /**
     * Method that send link for password
     * resetting on entered email address
     *
     * @param email a value of {@link Long}
     * @author Mariia Shchur
     */
    void sendLinkForPasswordResetting(String email);

    /**
     * Method that allow you to reset password.
     *
     * @param token
     * @param password new one
     * @author Mariia Shchur
     */
    void resetPassword(String token, String password);

    /**
     * Method for deleting users's account
     *
     * @param email
     * @author Mariia Shchur
     */
    void deleteAccount(String email);

    /**
     * Method that allow you to get {@link User} by ID.
     *
     * @param id a value of {@link Long}
     * @return {@link User}
     */
    User getById(long id);

    /**
     * Method that allow you to get {@link User} by email.
     *
     * @param email a value of {@link String}
     * @return {@link User}
     */
    User getUserByEmail(String email);

    /**
     * Method for set password and phone of user
     *
     * @param email {@link String}
     * @param userPasswordPhoneDto {@link UserPasswordPhoneDto}
     * @author Kravets Maryana
     */
    void setPasswordAndPhone(String email, UserPasswordPhoneDto userPasswordPhoneDto);

    UserDtoRole getUserRole(Principal principal);

    List<PermissionUserDto> getUsers();


}
