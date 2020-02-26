package com.softserve.rms.service;

import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;

import java.security.Principal;
import java.util.List;

public interface PermissionManagerService {

    List<PrincipalPermissionDto> findPrincipalWithAccess(Long id, Class clazz);

    void addPermission(PermissionDto permissionDto, Principal principal, Class clazz);

    void changeOwner(ChangeOwnerDto changeOwnerDto, Principal principal, Class clazz);

    void closePermissionForCertainUser(PermissionDto permissionDto, Principal principal, Class clazz);

    void closeAllPermissions(Long id, Principal principal, Class clazz);
}
