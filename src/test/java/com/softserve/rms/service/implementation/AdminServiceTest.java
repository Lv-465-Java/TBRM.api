package com.softserve.rms.service.implementation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.softserve.rms.dto.UserDto;;
import com.softserve.rms.entities.User;
import com.softserve.rms.repository.AdminRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(PowerMockRunner.class)
public class AdminServiceTest {
    @Mock
    AdminRepository adminRepository;
    @InjectMocks
    AdminServiceImpl service;

    @Test
    public void testFindAllMethodIsCall() {
        when(adminRepository.findAll()).thenReturn(ImmutableList.of());
        List<User> users = service.findAll();
        verify(adminRepository).findAll();
    }

    @Test
    public void testFindUsersByStatusIsCall() {
        when(adminRepository.getAllByEnabled(anyBoolean())).thenReturn(ImmutableList.of());
        List<User> users = service.findUsersByStatus(anyBoolean());
        verify(adminRepository).getAllByEnabled(anyBoolean());
    }

    @Test
    public void testFindByIdIsCall() {
        when(adminRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        Optional<User> user = service.findById(anyLong());
        verify(adminRepository).findById(anyLong());
    }

    @Test
    public void testCreateIsCall() {
        when(adminRepository.save(new User())).thenReturn(new User());
        service.create(new UserDto());
        verify(adminRepository).save(new User());
    }

    @Test
    public void testUpdateIsCall() {
        when(adminRepository.save(new User())).thenReturn(new User());
        service.update(new UserDto());
        verify(adminRepository).save(new User());
    }

    @Test
    public void testFindAllNotNull() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(service.findAll()).thenReturn(users);
        assertFalse(service.findAll().isEmpty());
    }

    @Test
    public void testFindByIdNotNull(){
        when(service.findById(anyLong())).thenReturn(Optional.of(new User()));
        assertNotNull(service.findById(anyLong()));
    }

    @Test
    public void testFindUsersByStatusNotNull() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(service.findUsersByStatus(anyBoolean())).thenReturn(users);
        assertFalse(service.findUsersByStatus(anyBoolean()).isEmpty());
    }
}