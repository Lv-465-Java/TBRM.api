package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.service.PermissionManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PermissionManagerServiceImpl implements PermissionManagerService {

    private final MutableAclService mutableAclService;

    @Autowired
    public PermissionManagerServiceImpl(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    @Transactional
    @Override
    public void addPermissionToResourceTemplate(PermissionDto permissionDto) {
        MutableAcl acl;
        Sid sid;
        Permission permission;

        sid = permissionDto.isPrincipal()
                ? new PrincipalSid(permissionDto.getRecipient()) :  new GrantedAuthoritySid(permissionDto.getRecipient());

        permission = permissionDto.getPermission().equalsIgnoreCase("read")
                ? BasePermission.READ : BasePermission.WRITE;

        ObjectIdentity oid = new ObjectIdentityImpl(ResourceTemplate.class, permissionDto.getResTempId());

        try {
            acl = mutableAclService.createAcl(oid);
            acl.setOwner(sid);
            acl.insertAce(acl.getEntries().size(), permission, sid, true);
            mutableAclService.updateAcl(acl);
        } catch (AlreadyExistsException e) {
           //TODO
        }
    }

    @Override
    public void updatePermissionForResourceTemplate(PermissionDto permissionDto) {
        MutableAcl acl;
        Sid sid;
        Permission permission;

        sid = permissionDto.isPrincipal()
                ? new PrincipalSid(permissionDto.getRecipient()) :  new GrantedAuthoritySid(permissionDto.getRecipient());

        permission = permissionDto.getPermission().equalsIgnoreCase("read")
                ? BasePermission.READ : BasePermission.WRITE;

        ObjectIdentity oid = new ObjectIdentityImpl(ResourceTemplate.class, permissionDto.getResTempId());

        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
            acl.setOwner(sid);
            acl.insertAce(acl.getEntries().size(), permission, sid, true);
            mutableAclService.updateAcl(acl);
        } catch (NotFoundException e) {
            //TODO
        }
    }

    @Override
    public boolean closePermissionForCertainUser(long productId, String sidName, Permission permission) {
        return false;
    }

    @Override
    public boolean closeAllPermissionsToResource(long productId) {
        return false;
    }
}
