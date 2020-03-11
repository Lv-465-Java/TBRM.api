package com.softserve.rms.service;

import com.softserve.rms.dto.UserDto;
import com.softserve.rms.dto.UserDtoRole;
import com.softserve.rms.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService extends BaseService<User, UserDto> {

    Page<UserDto> findUsersByStatus(boolean status, Integer page, Integer pageSize);

    void editUserRole(UserDtoRole userDto, Long id);

    void deleteUser(Long id);
}
