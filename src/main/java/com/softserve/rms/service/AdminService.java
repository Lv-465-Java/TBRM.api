package com.softserve.rms.service;

import com.softserve.rms.dto.UserDto;
import com.softserve.rms.dto.UserDtoRole;
import com.softserve.rms.entities.User;

import java.util.List;

public interface AdminService extends BaseService<User, UserDto> {

    List<UserDto> findUsersByStatus(boolean status);

    void editUserRole(UserDtoRole userDto, Long id);

    void deleteUser(Long id);
}
