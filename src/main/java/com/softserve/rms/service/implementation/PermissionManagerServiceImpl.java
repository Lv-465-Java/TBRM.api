package com.softserve.rms.service.implementation;

import com.softserve.rms.service.PermissionManagerService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.Permission;

public class PermissionManagerServiceImpl implements PermissionManagerService {
    private MutableAclService mutableAclService;

    @Override
    public boolean closePermissionForCertainUser(long productId, String sidName, Permission permission) {
        return false;
    }

    @Override
    public boolean closeAllPermissionsToResource(long productId) {
        return false;
    }
}
