package com.softserve.rms.service.impl;

import com.softserve.rms.dto.UserDto;
import com.softserve.rms.dto.UserDtoRole;
import com.softserve.rms.entities.User;
import com.softserve.rms.repository.AdminRepository;
import com.softserve.rms.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminRepository adminRepository;
    private ModelMapper modelMapper;

    public AdminServiceImpl() {
        modelMapper = new ModelMapper();
    }
    /**
     * Method returns list of all users
     *
     * @return List of all users
     * @author Ivan Syniuk
     */
    @Override
    public List<User> findAll() {
        return adminRepository.findAll();
    }

    /**
     * Method returns list of users by status
     * @param status of {@link User}
     * @return List of users by status
     * @author Ivan Syniuk
     */
    @Override
    public List<User> findUsersByStatus(boolean status) {
        return adminRepository.getAllByEnabled(status);
    }

    /**
     * Method returns Optional of user by id
     * @param id of {@link User}
     * @return Optional of user by id
     * @author Ivan Syniuk
     */
    @Override
    public Optional<User> findById(Long id) {
        return adminRepository.findById(id);
    }

    /**
     * Method create user
     *
     * @param entity {@link UserDto}
     * @author Ivan Syniuk
     */
    @Override
    public void create(UserDto entity) {
        adminRepository.save(modelMapper.map(entity, User.class));
    }

    /**
     * Method update user
     *
     * @param entity {@link UserDto}
     * @author Ivan Syniuk
     */
    @Override
    public void update(UserDto entity) {
        adminRepository.save(modelMapper.map(entity, User.class));
    }

    /**
     * Method update role on {@link User}
     *
     * @param userDto {@link UserDtoRole}
     * @param id {@link User}
     * @author Ivan Syniuk
     */
    @Transactional
    @Override
    public void editUserRole(UserDtoRole userDto, Long id) {
        adminRepository.updateUserRoleById(userDto.getRole(), id);
    }
    /**
     * Method set role on guest and set status on false {@link User}
     *
     * @param id {@link User}
     * @author Ivan Syniuk
     */
    @Transactional
    @Override
    public void deleteUser(Long id) {
        adminRepository.deleteRoleById(id);
    }

}