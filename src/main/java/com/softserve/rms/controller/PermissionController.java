package com.softserve.rms.controller;

import com.softserve.rms.dto.PermissionDto;
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
    private ResourceTemplateRepository resourceTemplateRepository;

    @Autowired
    public PermissionController(PermissionManagerService permissionManagerService) {
        this.permissionManagerService = permissionManagerService;
    }

    @GetMapping
    public List<ResourceTemplate> getAll() {
        PermissionDto permissionDto = new PermissionDto(
                3L,
                "man2",
                "read",
                true
        );
        //permissionManagerService.addPermissionToResourceTemplate(permissionDto);
        //permissionManagerService.updatePermissionForResourceTemplate(permissionDto);
        //permissionManagerService.closeAllPermissionsToResource(2L);
        //permissionManagerService.closePermissionForCertainUser(permissionDto);
        return resourceTemplateRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAclByResourceTemplateId(@PathVariable Long id, Principal principal) {
        permissionManagerService.closeAllPermissionsToResource(id, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteAceForCertainUser(PermissionDto permissionDto, Principal principal) {
        permissionManagerService.closePermissionForCertainUser(permissionDto, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
