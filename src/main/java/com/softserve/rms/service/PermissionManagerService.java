package com.softserve.rms.service;

import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;

import java.security.Principal;
import java.util.List;

public interface PermissionManagerService {

    List<PrincipalPermissionDto> findPrincipalWithAccessToResourceTemplate(Long id);

    void addPermissionForResourceTemplate(PermissionDto permissionDto, Principal principal);

    boolean closePermissionForCertainUser(long productId, String sidName, Permission permission);

    boolean closeAllPermissionsToResource(long productId);
}
