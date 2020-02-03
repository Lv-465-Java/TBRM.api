package com.softserve.rms.service;

import com.softserve.rms.dto.PermissionDto;
import org.springframework.security.acls.model.Permission;

import java.security.Principal;

public interface PermissionManagerService {

    void addPermissionToResourceTemplate(PermissionDto permissionDto);

    void updatePermissionForResourceTemplate(PermissionDto permissionDto);

    void closePermissionForCertainUser(PermissionDto permissionDto, Principal principal);

    void closeAllPermissionsToResource(Long resourceTemplateId, Principal principal);
}
