package com.softserve.rms.service.implementation;

import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.service.PermissionManagerService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;

import javax.transaction.Transactional;
import java.util.List;

public class PermissionManagerServiceImpl implements PermissionManagerService {
    private MutableAclService mutableAclService;

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
                if(entry.getSid().equals(sid) && entry.getPermission().equals(permission)) {
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
