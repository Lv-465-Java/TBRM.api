package com.softserve.rms.service;

import org.springframework.security.acls.model.Permission;

public interface PermissionManagerService {
    boolean closePermissionForCertainUser(Long productId, String sidName, Permission permission);

    boolean closeAllPermissionsToResource(Long productId);
}
