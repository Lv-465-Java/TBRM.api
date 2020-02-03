package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.PermissionException;
import com.softserve.rms.service.PermissionManagerService;
import com.softserve.rms.util.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@PreAuthorize("hasRole('MANAGER')")
@Service
public class PermissionManagerServiceImpl implements PermissionManagerService {

    private final MutableAclService mutableAclService;
    private final Formatter formatter;

    @Autowired
    public PermissionManagerServiceImpl(MutableAclService mutableAclService, JdbcTemplate jdbcTemplate, Formatter formatter) {
        this.mutableAclService = mutableAclService;
        this.formatter = formatter;
    }

    /**
     * Method returns list of principal with permissions for particular object
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
            for (AccessControlEntry ace: aclEntries){
                principalPermissionDto = new PrincipalPermissionDto(formatter.sidFormatter(ace.getSid().toString()),
                        formatter.permissionFormatter(ace.getPermission().getPattern()));
                principalsWithPermissions.add(principalPermissionDto);
            }
        } catch (NotFoundException e){
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
            System.out.println(formatter.sidFormatter(acl.getOwner().toString()));
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

    @Override
    public boolean closePermissionForCertainUser(long productId, String sidName, Permission permission) {
        return false;
    }

    @Override
    public boolean closeAllPermissionsToResource(long productId) {
        return false;
    }

}
