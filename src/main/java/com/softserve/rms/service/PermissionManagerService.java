package com.softserve.rms.service;

import com.softserve.rms.dto.PermissionDto;
import org.springframework.security.acls.model.Permission;

public interface PermissionManagerService {

    void addPermissionToResourceTemplate(PermissionDto permissionDto);

    void updatePermissionForResourceTemplate(PermissionDto permissionDto);

    boolean closePermissionForCertainUser(Long productId, String sidName, Permission permission);

    boolean closeAllPermissionsToResource(Long productId);
}
