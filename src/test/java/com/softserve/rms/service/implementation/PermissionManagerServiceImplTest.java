package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.PermissionException;
import com.softserve.rms.security.mappers.PermissionMapper;
import com.softserve.rms.util.Formatter;
import com.sun.security.auth.UserPrincipal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.AclImpl;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
public class PermissionManagerServiceImplTest {
    @Mock
    private PermissionMapper permissionMapper;

    @Mock
    private MutableAclService mutableAclService;

    @Mock
    private Formatter formatter;

    @InjectMocks
    private PermissionManagerServiceImpl permissionManagerService;

    private Principal principal;
    private MutableAcl mutableAcl;
    private PermissionDto permissionDto;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        permissionManagerService = PowerMockito.spy(new PermissionManagerServiceImpl
                (mutableAclService, permissionMapper, formatter));
        AclAuthorizationStrategyImpl authorizationStrategy = PowerMockito.mock(AclAuthorizationStrategyImpl.class);
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(ResourceTemplate.class, 1L);
        principal = new UserPrincipal("owner");
        Sid sid = new PrincipalSid("owner");
        mutableAcl = new AclImpl(objectIdentity, 1L, authorizationStrategy, null, null, null, false, sid);
        permissionDto = new PermissionDto(1L, "manager", "read", true);
    }

    @Test
    public void closePermissionForCertainUserOk() {
        try {
            PowerMockito.whenNew(ObjectIdentityImpl.class).withArguments(any(), any()).thenReturn(new ObjectIdentityImpl(ResourceTemplate.class, 1L));
            PowerMockito.whenNew(PrincipalSid.class).withArguments(anyString()).thenReturn(new PrincipalSid(anyString()));
        } catch (Exception ignored) {
        }
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("owner").when(formatter).sidFormatter(anyString());
        permissionManagerService.closePermissionForCertainUser(permissionDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void closePermissionForCertainUserFailAccess() {
        try {
            PowerMockito.whenNew(ObjectIdentityImpl.class).withArguments(any(), any()).thenReturn(new ObjectIdentityImpl(ResourceTemplate.class, 1L));
            PowerMockito.whenNew(PrincipalSid.class).withArguments(anyString()).thenReturn(new PrincipalSid(anyString()));
        } catch (Exception ignored) {
        }
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("not owner").when(formatter).sidFormatter(anyString());
        permissionManagerService.closePermissionForCertainUser(permissionDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void closePermissionForCertainUserFailFound() {
        try {
            PowerMockito.whenNew(ObjectIdentityImpl.class).withArguments(any(), any()).thenReturn(new ObjectIdentityImpl(ResourceTemplate.class, 1L));
            PowerMockito.whenNew(PrincipalSid.class).withArguments(anyString()).thenReturn(new PrincipalSid(anyString()));
        } catch (Exception ignored) {
        }
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("owner").when(formatter).sidFormatter(anyString());
        doThrow(new NotFoundException("fail found")).when(mutableAclService).updateAcl(any(MutableAcl.class));
        permissionManagerService.closePermissionForCertainUser(permissionDto, principal);
    }

    @Test
    public void closeAllPermissionsToResourceOk() {
        try {
            PowerMockito.whenNew(ObjectIdentityImpl.class).withArguments(any(), any()).thenReturn(new ObjectIdentityImpl(ResourceTemplate.class, 1L));
        } catch (Exception ignored) {
        }
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("owner").when(formatter).sidFormatter(anyString());
        doNothing().when(mutableAclService).deleteAcl(any(ObjectIdentityImpl.class), anyBoolean());
        doCallRealMethod().when(permissionManagerService).closeAllPermissionsToResource(anyLong(), any(Principal.class));
        permissionManagerService.closeAllPermissionsToResource(1L, principal);
        verify(permissionManagerService, times(1)).closeAllPermissionsToResource(1L, principal);
    }

    @Test(expected = PermissionException.class)
    public void closeAllPermissionsToResourceFailFound() {
        try {
            PowerMockito.whenNew(ObjectIdentityImpl.class).withArguments(any(), any()).thenReturn(new ObjectIdentityImpl(ResourceTemplate.class, 1L));
        } catch (Exception ignored) {
        }
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("owner").when(formatter).sidFormatter(anyString());
        doThrow(new NotFoundException("fail found")).when(mutableAclService).deleteAcl(any(ObjectIdentityImpl.class), anyBoolean());
        permissionManagerService.closeAllPermissionsToResource(1L, principal);
    }

    @Test(expected = PermissionException.class)
    public void closeAllPermissionsToResourceFailAccess() {
        try {
            PowerMockito.whenNew(ObjectIdentityImpl.class).withArguments(any(), any()).thenReturn(new ObjectIdentityImpl(ResourceTemplate.class, 1L));
        } catch (Exception ignored) {
        }
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("not owner").when(formatter).sidFormatter(anyString());
        doNothing().when(mutableAclService).deleteAcl(any(ObjectIdentityImpl.class), anyBoolean());
        permissionManagerService.closeAllPermissionsToResource(1L, principal);
    }
}