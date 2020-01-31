package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.service.PermissionManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                ? new PrincipalSid(permissionDto.getRecipient()) : new GrantedAuthoritySid(permissionDto.getRecipient());

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
                ? new PrincipalSid(permissionDto.getRecipient()) : new GrantedAuthoritySid(permissionDto.getRecipient());

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

    @Transactional
    @Override
    public boolean closePermissionForCertainUser(Long resourceId, String sidName, Permission permission) {
        MutableAcl acl;
        boolean closed = true;
        PrincipalSid sid = new PrincipalSid(sidName);
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(ResourceTemplate.class, resourceId);
        int acePosition = 0;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(objectIdentity);
            List<AccessControlEntry> aces = acl.getEntries();
            for (AccessControlEntry entry : aces) {
                if (entry.getSid().equals(sid) && entry.getPermission().equals(permission)) {
                    acl.deleteAce(acePosition);
                    acePosition--;
                }
                acePosition++;
            }
            mutableAclService.updateAcl(acl);
        } catch (NotFoundException | DataAccessException e) {
            closed = false;
        }
        return closed;
    }

    @Transactional
    @Override
    public boolean closeAllPermissionsToResource(Long resourceId) {
        boolean closed = true;
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(resourceId);
        try {
            mutableAclService.deleteAcl(objectIdentity, false);
        } catch (DataAccessException e) {
            closed = false;
        }
        return closed;
    }
}