package com.softserve.rms.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.UserDto;
import com.softserve.rms.dto.UserDtoRole;
import com.softserve.rms.service.AdminService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@RestController
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping(value = "/admin/user")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = adminService.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping(value = "/admin/users")
    public ResponseEntity<List<UserDto>> getUsersByStatus(@RequestParam boolean status) {
        List<UserDto> users = adminService.findUsersByStatus(status);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201,message = HttpStatuses.CREATED),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping(value = "/admin/user")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto newUser) {
        adminService.create(newUser);
        return new ResponseEntity<>(newUser,HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @PatchMapping(value = "/admin/user/{id}")
    public ResponseEntity<UserDtoRole> setRole(@RequestBody UserDtoRole updateUser, @PathVariable("id") String id) {
        adminService.editUserRole(updateUser, Long.valueOf(id));
        return new ResponseEntity<>(updateUser,HttpStatus.OK);
    }
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @PutMapping(value = "/admin/user/{id}")
    public void deleteUser(@PathVariable("id") String id) {
        adminService.deleteUser(Long.valueOf(id));
    }
}