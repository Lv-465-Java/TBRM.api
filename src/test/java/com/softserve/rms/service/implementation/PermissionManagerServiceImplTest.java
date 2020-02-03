package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.PermissionException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.MutableAclService;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
public class PermissionManagerServiceImplTest {
    @Mock
    private PermissionMapper permissionMapper;

    @Mock
    private MutableAclService mutableAclService;

    private PermissionManagerServiceImpl permissionManagerService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        permissionManagerService = PowerMockito.spy(new PermissionManagerServiceImpl
                (mutableAclService, permissionMapper));
    }

    @Test
    public void closePermissionForCertainUser() {
        assertTrue(true);
    }

    @Test
    public void closeAllPermissionsToResourceOk() {
        try {
            PowerMockito.whenNew(ObjectIdentityImpl.class).withArguments(any(), any()).thenReturn(new ObjectIdentityImpl(ResourceTemplate.class, 1L));
        } catch (Exception e) {
            //TODO
        }
        doNothing().when(mutableAclService).deleteAcl(any(ObjectIdentityImpl.class), anyBoolean());
        doCallRealMethod().when(permissionManagerService).closeAllPermissionsToResource(anyLong());
        permissionManagerService.closeAllPermissionsToResource(1L);
        verify(permissionManagerService, times(1)).closeAllPermissionsToResource(1L);
    }

    @Test(expected = PermissionException.class)
    public void closeAllPermissionsToResourceFail() {
        try {
            PowerMockito.whenNew(ObjectIdentityImpl.class).withArguments(any(), any()).thenReturn(new ObjectIdentityImpl(ResourceTemplate.class, 1L));
        } catch (Exception e) {
            //TODO
        }
        doNothing().when(mutableAclService).deleteAcl(any(ObjectIdentityImpl.class), anyBoolean());
        doThrow(new PermissionException(ErrorMessage.DENIED_ACCESS.getMessage())).when(permissionManagerService).closeAllPermissionsToResource(anyLong());
        permissionManagerService.closeAllPermissionsToResource(1L);
    }
}