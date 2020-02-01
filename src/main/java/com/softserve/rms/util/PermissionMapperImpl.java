package com.softserve.rms.util;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.stereotype.Component;

/**
 * This Class is used to convert string permission
 * to object of type Permission.
 */
@Component
public class PermissionMapperImpl implements PermissionMapper{
    /**
     * Method converts string permission
     * to object of type Permission.
     *
     * @param permission in string form
     * @return integer mask
     */
    @Override
    public Permission getMask(String permission) {
        Permission basePermission = null;
        if (permission.equalsIgnoreCase("read")) {
            basePermission = BasePermission.READ;
        } else if (permission.equalsIgnoreCase("write")) {
            basePermission = BasePermission.WRITE;
        }
        return basePermission;
    }
}