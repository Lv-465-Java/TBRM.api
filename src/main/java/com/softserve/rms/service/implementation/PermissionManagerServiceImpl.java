package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.PermissionException;
import com.softserve.rms.security.mappers.PermissionMapper;
import com.softserve.rms.service.PermissionManagerService;
import com.softserve.rms.util.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("hasRole('MANAGER')")
@Service
public class PermissionManagerServiceImpl implements PermissionManagerService {

    private final MutableAclService mutableAclService;
    private final PermissionMapper permissionMapper;
    private final Formatter formatter;

    /**
     * Constructor with parameters.
     *
     * @param mutableAclService perform crud operations with acl
     * @param permissionMapper map string permission to integer mask
     * @param formatter format permission string retrieved from Sid object
     *
     * @author Artur Sydor
     */
    @Autowired
    public PermissionManagerServiceImpl(MutableAclService mutableAclService, PermissionMapper permissionMapper, Formatter formatter) {
        this.mutableAclService = mutableAclService;
        this.permissionMapper = permissionMapper;
        this.formatter = formatter;
    }

    /**
     * Method returns list of principal with permissions for particular object
     *
     * @param id of {@link ResourceTemplate}
     * @return List of principal with access to object
     */
    @Override
    public List<PrincipalPermissionDto> findPrincipalWithAccessToResourceTemplate(Long id) {
        PrincipalPermissionDto principalPermissionDto;
        List<PrincipalPermissionDto> principalsWithPermissions = new ArrayList<>();
        try {
            Acl acl = mutableAclService.readAclById(new ObjectIdentityImpl(ResourceTemplate.class, id));
            List<AccessControlEntry> aclEntries = acl.getEntries();
            for (AccessControlEntry ace : aclEntries) {
                principalPermissionDto = new PrincipalPermissionDto(formatter.sidFormatter(ace.getSid().toString()),
                        formatter.permissionFormatter(ace.getPermission().getPattern()));
                principalsWithPermissions.add(principalPermissionDto);
            }
        } catch (NotFoundException e) {
            throw new PermissionException(ErrorMessage.PRINCIPAL_NOT_FOUND.getMessage());
        }
        return principalsWithPermissions;
    }


    /**
     * Method add/update permission on @{@link ResourceTemplate}
     *
     * @param permissionDto {@link PermissionDto}
     * @param principal authenticated user
     */
    @Transactional
    @Override
    public void addPermissionForResourceTemplate(PermissionDto permissionDto, Principal principal) {
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
            if (!formatter.sidFormatter(acl.getOwner().toString()).equals(principal.getName())){
                throw new PermissionException(ErrorMessage.ACCESS_DENIED.getMessage());
            }
        } catch (NotFoundException e) {
            acl = mutableAclService.createAcl(oid);
            acl.setOwner(sid);
        }
        acl.insertAce(acl.getEntries().size(), permission, sid, true);
        mutableAclService.updateAcl(acl);
    }

    /**
     * Method deletes one permission for
     * certain user.
     *
     * @param permissionDto transfer information about certain ACE
     * @param principal authenticated user
     *
     * @author Artur Sydor
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
                throw new PermissionException(ErrorMessage.ACCESS_DENIED.getMessage());
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
     *
     * @author Artur Sydor
     */
    @Transactional
    @Override
    public void closeAllPermissionsToResource(Long resourceId, Principal principal) {
        MutableAcl acl;
        try {
            ObjectIdentity objectIdentity = new ObjectIdentityImpl(ResourceTemplate.class, resourceId);
            acl = (MutableAcl) mutableAclService.readAclById(objectIdentity);
            if(!formatter.sidFormatter(acl.getOwner().toString()).equals(principal.getName())) {
                throw new PermissionException(ErrorMessage.ACCESS_DENIED.getMessage());
            }
            mutableAclService.deleteAcl(objectIdentity, false);
        } catch (NotFoundException e) {
            throw new PermissionException(ErrorMessage.PERMISSION_NOT_FOUND.getMessage());
        }
    }
}