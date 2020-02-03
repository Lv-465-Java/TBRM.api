package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.PermissionException;
import com.softserve.rms.security.mappers.PermissionMapper;
import com.softserve.rms.service.PermissionManagerService;
import com.softserve.rms.util.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@PreAuthorize("hasRole('MANAGER')")
public class PermissionManagerServiceImpl implements PermissionManagerService {

    private final MutableAclService mutableAclService;
    private final PermissionMapper permissionMapper;
    private final Formatter formatter;

    @Autowired
    public PermissionManagerServiceImpl(MutableAclService mutableAclService, PermissionMapper permissionMapper, Formatter formatter) {
        this.mutableAclService = mutableAclService;
        this.permissionMapper = permissionMapper;
        this.formatter = formatter;
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

    /**
     * Method deletes one permission for
     * certain user.
     *
     * @param permissionDto transfer information about certain ACE
     * @param principal authenticated user
     */
    @Transactional
    @Override
    public void closePermissionForCertainUser(PermissionDto permissionDto, Principal principal) {
        MutableAcl acl;
        Sid sid = permissionDto.isPrincipal()
                ? new PrincipalSid(permissionDto.getRecipient()) : new GrantedAuthoritySid(permissionDto.getRecipient());
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(ResourceTemplate.class, permissionDto.getResTempId());
        int acePosition = 0;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(objectIdentity);
            if(!formatter.sidFormatter(acl.getOwner().toString()).equals(principal.getName())) {
                throw new PermissionException(ErrorMessage.DENIED_ACCESS.getMessage());
            }
            List<AccessControlEntry> aces = acl.getEntries();
            for (AccessControlEntry entry : aces) {
                if (entry.getSid().equals(sid)
                        && entry.getPermission().equals(permissionMapper.getMask(permissionDto.getPermission()))) {
                    acl.deleteAce(acePosition);
                    acePosition--;
                }
                acePosition++;
            }
            mutableAclService.updateAcl(acl);
        } catch (NotFoundException e) {
            throw new PermissionException(ErrorMessage.PERMISSION_NOT_FOUND.getMessage());
        }
    }

    /**
     * Method deletes ACL for certain resource template.
     *
     * @param resourceId id of resource template
     * @param principal authenticated user
     */
    @Transactional
    @Override
    public void closeAllPermissionsToResource(Long resourceId, Principal principal) {
        MutableAcl acl;
        try {
            ObjectIdentity objectIdentity = new ObjectIdentityImpl(ResourceTemplate.class, resourceId);
            acl = (MutableAcl) mutableAclService.readAclById(objectIdentity);
            if(!formatter.sidFormatter(acl.getOwner().toString()).equals(principal.getName())) {
                throw new PermissionException(ErrorMessage.DENIED_ACCESS.getMessage());
            }
            mutableAclService.deleteAcl(objectIdentity, false);
        } catch (NotFoundException e) {
            throw new PermissionException(ErrorMessage.PERMISSION_NOT_FOUND.getMessage());
        }
    }
}