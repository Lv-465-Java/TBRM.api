package com.softserve.rms.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.RoleDto;
import com.softserve.rms.dto.UserDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.service.AdminService;
import com.softserve.rms.service.UserHistoryService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@RestController
public class AdminController {

    private final AdminService adminService;
    private final UserHistoryService userHistoryService;

    @Autowired
    public AdminController(AdminService adminService,UserHistoryService userHistoryService) {
        this.adminService = adminService;
        this.userHistoryService=userHistoryService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping(value = "/admin/user")
    public ResponseEntity<Page<UserDto>> getAllUsers(@RequestParam Optional<Integer> page,
                                                     @RequestParam Optional<Integer> pageSize) {
        Page<UserDto> users = adminService.findAll(page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping(value = "/admin/users")
    public ResponseEntity<Page<UserDto>> getUsersByStatus(@RequestParam boolean status,
                                                          @RequestParam Optional<Integer> page,
                                                          @RequestParam Optional<Integer> pageSize) {
        Page<UserDto> users = adminService.findUsersByStatus(status, page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5));
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
    public ResponseEntity setRole(@RequestBody RoleDto updateUser, @PathVariable("id") String id) {
        adminService.editUserRole(updateUser, Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).build();
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


    /**
     * Method that show all {@link User} history.
     *
     * @param id
     * @return list of all user's history
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("user/{id}")
    public ResponseEntity<List<Map<String,Object>>> getUserHistory(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(userHistoryService.getUserHistory(id));
    }

    /**
     * Method that returns all deleted accounts
     *
     * @return list of all deleted accounts
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/deleted_accounts")
    public ResponseEntity<List<Map<String, Object>>> getAllDeletedAccounts(){
        return ResponseEntity.status(HttpStatus.OK).body(userHistoryService.getDeletedAccounts());
    }

    /**
     * Method that returns all history flow
     *
     * @return list of all accounts
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/all_history")
    public ResponseEntity<List<Map<String, Object>>> getHistory(){
        return ResponseEntity.status(HttpStatus.OK).body(userHistoryService.getAllHistory());
    }

    /**
     * Method that returns all users history by accurate data
     *
     * @return list of all accounts
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403 ,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/{date}")
    public ResponseEntity<List<Map<String, Object>>> getAllByDate(@PathVariable String date){
        return ResponseEntity.status(HttpStatus.OK).body(userHistoryService.getAllByData(date));
    }
    }