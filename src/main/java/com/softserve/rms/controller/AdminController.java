package com.softserve.rms.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.rms.dto.UserDto;
import com.softserve.rms.dto.UserDtoRole;
import com.softserve.rms.entity.User;
import com.softserve.rms.service.Impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@RestController
public class AdminController {
    @Autowired
    private AdminServiceImpl adminService;

    @GetMapping(value = "/admin/user")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/users")
    public ResponseEntity<List<User>> getUsersByStatus(@RequestParam boolean status) {
        List<User> users = adminService.findUsersByStatus(status);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping(value = "/admin/user")
    public void addUser(@RequestBody UserDto newUser) {
        adminService.create(newUser);
    }

    @PatchMapping(value = "/admin/user/{id}")
    public void setRole(@RequestBody UserDtoRole updateUser, @PathVariable("id") String id) {
        adminService.editUserRole(updateUser, Long.valueOf(id));
    }

    @PutMapping(value = "/admin/user/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        adminService.deleteUser(Long.valueOf(id));
    }
}