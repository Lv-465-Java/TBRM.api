package com.softserve.rms.controller;

import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.service.PermissionManagerService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionManagerService permissionManagerService;

    @Autowired
    public PermissionController(PermissionManagerService permissionManagerService) {
        this.permissionManagerService = permissionManagerService;
    }
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 403,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<PrincipalPermissionDto>> getUsersWithAccess(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(permissionManagerService.findPrincipalWithAccessToResourceTemplate(Long.parseLong(id)));
    }
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 403,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping
    public ResponseEntity<PermissionDto> addPermissionToResourceTemplate(@RequestBody PermissionDto permissionDto, Principal principal){
        permissionManagerService.addPermissionForResourceTemplate(permissionDto, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 403,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAclByResourceTemplateId(@PathVariable Long id, Principal principal) {
        permissionManagerService.closeAllPermissionsToResource(id, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = HttpStatuses.OK),
            @ApiResponse(code = 403,message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping
    public ResponseEntity<Object> deleteAceForCertainUser(PermissionDto permissionDto, Principal principal) {
        permissionManagerService.closePermissionForCertainUser(permissionDto, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
