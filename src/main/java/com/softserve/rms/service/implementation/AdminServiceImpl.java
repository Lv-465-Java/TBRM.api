package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.UserDto;
import com.softserve.rms.dto.UserDtoRole;
import com.softserve.rms.entities.User;
import com.softserve.rms.repository.AdminRepository;
import com.softserve.rms.service.AdminService;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditQuery;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PreAuthorize("hasRole('ADMIN')")
@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private ModelMapper modelMapper;
   @Autowired
    public AdminServiceImpl(AdminRepository adminRepository) {
        modelMapper = new ModelMapper();
        this.adminRepository = adminRepository;
    }
    /**
     * Method returns list of all users
     *
     * @return List of all users
     * @author Ivan Syniuk
     */
    @Override
    public List<UserDto> findAll() {
        List<User> users = adminRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Method returns list of users by status
     * @param status of {@link User}
     * @return List of users by status
     * @author Ivan Syniuk
     */
    @Override
    public List<UserDto> findUsersByStatus(boolean status) {
        List<User> users = adminRepository.getAllByEnabled(status);
        return users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
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