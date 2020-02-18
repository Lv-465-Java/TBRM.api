package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.PermissionException;
import com.softserve.rms.repository.UserRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RunWith(PowerMockRunner.class)
public class PermissionManagerServiceImplTest {
    @Mock
    private PermissionMapper permissionMapper;

    @Mock
    private MutableAclService mutableAclService;

    @Mock
    private MutableAcl aclService;

    @Mock
    private Formatter formatter;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PermissionManagerServiceImpl permissionManagerService;

    private Principal principal;
    private MutableAcl mutableAcl;
    private PermissionDto permissionDto;
    private ChangeOwnerDto changeOwnerDto;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        permissionManagerService = PowerMockito.spy(new PermissionManagerServiceImpl
                (mutableAclService, permissionMapper, formatter, userRepository));
        AclAuthorizationStrategyImpl authorizationStrategy = PowerMockito.mock(AclAuthorizationStrategyImpl.class);
        ObjectIdentity objectIdentity = new ObjectIdentityImpl(ResourceTemplate.class, 1L);
        principal = new UserPrincipal("owner");
        Sid sid = new PrincipalSid("owner");
        mutableAcl = new AclImpl(objectIdentity, 1L, authorizationStrategy, null, null, null, false, sid);
        permissionDto = new PermissionDto(1L, "manager", "read", true);
        changeOwnerDto = new ChangeOwnerDto(2L, "manager");
    }

    @Test
    public void findPrincipalWithAccessToResourceTemplateSuccess(){
        List<AccessControlEntry> entries = Arrays.asList();
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn(entries).when(aclService).getEntries();
        permissionManagerService.findPrincipalWithAccessToResourceTemplate(anyLong());
    }

    @Test(expected = PermissionException.class)
    public void findPrincipalWithAccessToResourceTemplateFail(){
        doThrow(new NotFoundException("fail found")).when(mutableAclService).readAclById(any());
        permissionManagerService.findPrincipalWithAccessToResourceTemplate(anyLong());
    }

    @Test
    public void addPermissionForResourceTemplateSuccess(){
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("owner").when(formatter).sidFormatter(anyString());
        permissionManagerService.addPermissionForResourceTemplate(permissionDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void addPermissionForResourceTemplateFail(){
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("not owner").when(formatter).sidFormatter(anyString());
        permissionManagerService.addPermissionForResourceTemplate(permissionDto, principal);
    }

    @Test
    public void changeOwnerForResourceTemplateSuccess(){
        doReturn(Optional.of(new User())).when(userRepository).findUserByEmail(anyString());
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("owner").when(formatter).sidFormatter(anyString());
        doNothing().when(permissionManagerService).closePermissionForCertainUser(any(), any());
        permissionManagerService.changeOwnerForResourceTemplate(changeOwnerDto, principal);
    }

    @Test(expected = com.softserve.rms.exceptions.NotFoundException.class)
    public void changeOwnerForResourceTemplateFailFound(){
        doReturn(Optional.empty()).when(userRepository).findUserByEmail(anyString());
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("owner").when(formatter).sidFormatter(anyString());
        doNothing().when(permissionManagerService).closePermissionForCertainUser(any(), any());
        permissionManagerService.changeOwnerForResourceTemplate(changeOwnerDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void changeOwnerForResourceTemplateFailAccess(){
        doReturn(Optional.of(new User())).when(userRepository).findUserByEmail(anyString());
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("not owner").when(formatter).sidFormatter(anyString());
        doNothing().when(permissionManagerService).closePermissionForCertainUser(any(), any());
        permissionManagerService.changeOwnerForResourceTemplate(changeOwnerDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void changeOwnerForResourceTemplateFailFoundAcl(){
        doReturn(Optional.of(new User())).when(userRepository).findUserByEmail(anyString());
        doThrow(new NotFoundException("")).when(mutableAclService).readAclById(any());
        doReturn("owner").when(formatter).sidFormatter(anyString());
        doNothing().when(permissionManagerService).closePermissionForCertainUser(any(), any());
        permissionManagerService.changeOwnerForResourceTemplate(changeOwnerDto, principal);
    }

    @Test
    public void closePermissionForCertainUserOk() {
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("owner").when(formatter).sidFormatter(anyString());
        permissionManagerService.closePermissionForCertainUser(permissionDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void closePermissionForCertainUserFailAccess() {
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("not owner").when(formatter).sidFormatter(anyString());
        permissionManagerService.closePermissionForCertainUser(permissionDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void closePermissionForCertainUserFailFound() {
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("owner").when(formatter).sidFormatter(anyString());
        doThrow(new NotFoundException("fail found")).when(mutableAclService).updateAcl(any(MutableAcl.class));
        permissionManagerService.closePermissionForCertainUser(permissionDto, principal);
    }

    @Test
    public void closeAllPermissionsToResourceOk() {
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("owner").when(formatter).sidFormatter(anyString());
        doNothing().when(mutableAclService).deleteAcl(any(ObjectIdentityImpl.class), anyBoolean());
        doCallRealMethod().when(permissionManagerService).closeAllPermissionsToResource(anyLong(), any(Principal.class));
        permissionManagerService.closeAllPermissionsToResource(1L, principal);
        verify(permissionManagerService, times(1)).closeAllPermissionsToResource(1L, principal);
    }

    @Test(expected = PermissionException.class)
    public void closeAllPermissionsToResourceFailFound() {
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("owner").when(formatter).sidFormatter(anyString());
        doThrow(new NotFoundException("fail found")).when(mutableAclService).deleteAcl(any(ObjectIdentityImpl.class), anyBoolean());
        permissionManagerService.closeAllPermissionsToResource(1L, principal);
    }

    @Test(expected = PermissionException.class)
    public void closeAllPermissionsToResourceFailAccess() {
        doReturn(mutableAcl).when(mutableAclService).readAclById(any());
        doReturn("not owner").when(formatter).sidFormatter(anyString());
        doNothing().when(mutableAclService).deleteAcl(any(ObjectIdentityImpl.class), anyBoolean());
        permissionManagerService.closeAllPermissionsToResource(1L, principal);
    }
}