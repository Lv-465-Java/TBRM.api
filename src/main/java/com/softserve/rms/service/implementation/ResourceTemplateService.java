package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.service.PermissionManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.stereotype.Service;

@Service
public class ResourceTemplateService {

    private final ResourceTemplateRepository resourceTemplateRepository;
    private final PermissionManagerService permissionManagerService;

    @Autowired
    public ResourceTemplateService(ResourceTemplateRepository resourceTemplateRepository, PermissionManagerService permissionManagerService) {
        this.resourceTemplateRepository = resourceTemplateRepository;
        this.permissionManagerService = permissionManagerService;
    }

}
