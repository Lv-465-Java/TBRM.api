package com.softserve.rms.service;

import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;

import java.security.Principal;
import java.util.List;

public interface PermissionManagerService {

    List<PrincipalPermissionDto> findPrincipalWithAccessToResourceTemplate(Long id);

    void addPermissionForResourceTemplate(PermissionDto permissionDto, Principal principal);

    void changeOwnerForResourceTemplate(ChangeOwnerDto changeOwnerDto, Principal principal);

    void closePermissionForCertainUser(PermissionDto permissionDto, Principal principal);

    void closeAllPermissionsToResource(Long resourceTemplateId, Principal principal);
}
