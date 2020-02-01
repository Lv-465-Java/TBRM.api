package com.softserve.rms.controller;

import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.service.PermissionManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return resourceTemplateRepository.findAll();
    }
}
