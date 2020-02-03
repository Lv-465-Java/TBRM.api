package com.softserve.rms.controller;

import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.service.PermissionManagerService;
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
    public PermissionController(PermissionManagerService permissionManagerService, ResourceTemplateRepository resourceTemplateRepository) {
        this.permissionManagerService = permissionManagerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<PrincipalPermissionDto>> getUsersWithAccess(@PathVariable String id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(permissionManagerService.findPrincipalWithAccessToResourceTemplate(Long.parseLong(id)));
    }

    @PostMapping
    public ResponseEntity<PermissionDto> addPermissionToResourceTemplate(@RequestBody PermissionDto permissionDto, Principal principal){
        permissionManagerService.addPermissionForResourceTemplate(permissionDto, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
