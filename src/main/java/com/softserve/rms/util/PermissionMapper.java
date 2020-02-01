package com.softserve.rms.util;

import org.springframework.security.acls.model.Permission;

public interface PermissionMapper {
    /**
     * Method converts string permission
     * to object of type Permission.
     *
     * @param permission in string form
     * @return integer mask
     */
    public Permission getMask(String permission);
}
