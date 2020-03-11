package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.entities.*;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.exceptions.PermissionException;
import com.softserve.rms.exceptions.resourseTemplate.*;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.repository.implementation.JooqDDL;
import com.softserve.rms.service.GroupService;
import com.softserve.rms.util.EmailSender;
import com.softserve.rms.util.Formatter;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourceTemplateServiceImpl.class)
public class ResourceTemplateServiceTest {
    @InjectMocks
    private ResourceTemplateServiceImpl resourceTemplateService;
    @Mock
    private ResourceTemplateServiceImpl resourceTemplateService1;
    @Mock
    private ResourceTemplateRepository resourceTemplateRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private PermissionManagerServiceImpl permissionManagerService;
    @Mock
    private DSLContext dslContext;
    @Mock
    private Authentication authentication;
    @Mock
    private Principal principal;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private JooqDDL jooqDDL = PowerMockito.mock(JooqDDL.class);
    @Mock
    private EmailSender emailSender;
    @Mock
    private GroupService groupService;
    @Mock
    private Formatter formatter;

    private Role role = new Role(2L, "MANAGER");
    private User user = new User(1L, "testName", "testSurname", "testEmail", "any",
            "any", false, role,"imageurl","google","12444", Collections.emptyList(), null, Collections.emptyList());
    private ResourceTemplate resourceTemplate = new ResourceTemplate(1L, "name", "name",
            "description", false, user, Collections.emptyList(), Collections.emptyList());
    private ResourceTemplateSaveDTO resourceTemplateSaveDTO = new ResourceTemplateSaveDTO("name", "description");
    private ResourceTemplateDTO resourceTemplateDTO = new ResourceTemplateDTO(null, "name", "name",
            "description", false, user.getId(), null);
    private ResourceTemplateDTO resourceTempDTO = new ResourceTemplateDTO(1L, "name", "name",
            "description", false, user.getId(), Collections.emptyList());
    private Map<String, Object> map;

    @Before
    public void initializeMock() {
        resourceTemplateService = PowerMockito.spy(new ResourceTemplateServiceImpl(resourceTemplateRepository, userService,
                permissionManagerService, dslContext, jooqDDL, formatter, emailSender, groupService));
        JooqDDL jooqDDL = mock(JooqDDL.class);
    }

    @Test
    public void testSaveResourceTemplate() {
        when(resourceTemplateRepository.saveAndFlush(any())).thenReturn(resourceTemplate);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("");
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        PowerMockito.doNothing().when(resourceTemplateService).setAccessToTemplate(anyLong(), any(Principal.class));
        assertEquals(resourceTemplateDTO, resourceTemplateService.save(resourceTemplateSaveDTO));
    }

    @Test
    public void testFindDTOById() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        assertEquals(resourceTempDTO, resourceTemplateService.findDTOById(anyLong()));
    }

    @Test(expected = NotFoundException.class)
    public void testFindDTOByIdFail() {
        resourceTemplateService.findDTOById(null);
    }

    @Test
    public void testFindAll() {
        when(resourceTemplateRepository.findAll()).thenReturn(Collections.singletonList(resourceTemplate));
        List<ResourceTemplateDTO> resourceTemplateDTOs = Collections.singletonList(resourceTempDTO);
        assertEquals(resourceTemplateDTOs, resourceTemplateService.getAll());
    }

    @Test
    public void testFindAllPublished() {
        when(resourceTemplateRepository.findAllByIsPublishedIsTrue()).thenReturn(Collections.singletonList(resourceTemplate));
        List<ResourceTemplateDTO> resourceTemplateDTOs = Collections.singletonList(resourceTempDTO);
        assertEquals(resourceTemplateDTOs, resourceTemplateService.findAllPublishedTemplates());
    }

    @Test
    public void testFindAllByUserId() {
        when(resourceTemplateRepository.findAllByUserId(anyLong())).thenReturn(Collections.singletonList(resourceTemplate));
        List<ResourceTemplateDTO> resourceTemplateDTOs = Collections.singletonList(resourceTempDTO);
        assertEquals(resourceTemplateDTOs, resourceTemplateService.getAllByUserId(anyLong()));
    }

    @Test(expected = ResourceTemplateCanNotBeModified.class)
    public void testUpdateResourceTemplateWithPublishFalse() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        resourceTemplate.setIsPublished(true);
        ResourceTemplateDTO resultResourceTemplate = resourceTemplateService.checkIfTemplateCanBeUpdated(1L, map);
        assertEquals(resourceTempDTO, resultResourceTemplate);
    }

    @Test
    public void testUpdateResourceTemplateWithPublishTrue() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        ResourceTemplateDTO updatedDTO = new ResourceTemplateDTO(1L, "updated name", "updated_name",
                "updated description", false, user.getId(), Collections.emptyList());
        map = new HashMap<>();
        map.put("name", "updated name");
        map.put("description", "updated description");
        ResourceTemplateDTO temDto = resourceTemplateService.checkIfTemplateCanBeUpdated(1L, map);
        assertEquals(updatedDTO, temDto);
    }

    @Test
    public void testUpdateResourceTemplateWithDescription() throws Exception {
        ResourceTemplateDTO updatedDTO = new ResourceTemplateDTO(1L, "name", "name",
                "updated description", false, user.getId(), Collections.emptyList());
        map = new HashMap<>();
        map.put("description", "updated description");
        ResourceTemplateDTO resultResourceTemplate = Whitebox.invokeMethod(
                resourceTemplateService, "updateById", resourceTemplate, map);
        assertEquals(updatedDTO, resultResourceTemplate);
    }

    @Test
    public void testUpdateResourceTemplateWithName() throws Exception {
        ResourceTemplateDTO updatedDTO = new ResourceTemplateDTO(1L, "updated name", "updated_name",
                "description", false, user.getId(), Collections.emptyList());
        map = new HashMap<>();
        map.put("name", "updated name");
        ResourceTemplateDTO resultResourceTemplate = Whitebox.invokeMethod(
                resourceTemplateService, "updateById", resourceTemplate, map);
        assertEquals(updatedDTO, resultResourceTemplate);
    }

    @Test
    public void testDeleteByIdTrue() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        resourceTemplateService.checkIfTemplateCanBeDeleted(resourceTemplate.getId());
        verify(resourceTemplateRepository, times(1)).deleteById(resourceTemplate.getId());
    }

    @Test(expected = ResourceTemplateCanNotBeModified.class)
    public void testDeleteByIdFalse() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        resourceTemplate.setIsPublished(true);
        verify(resourceTemplateRepository, times(0)).deleteById(resourceTemplate.getId());
        resourceTemplateService.checkIfTemplateCanBeDeleted(resourceTemplate.getId());
    }

    @Test
    public void testDeleteById() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        doNothing().when(permissionManagerService).closeAllPermissions(anyLong(), any(), any(Class.class));
        resourceTemplateService.deleteById(resourceTemplate.getId());
        verify(resourceTemplateRepository, times(1)).deleteById(resourceTemplate.getId());
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteByIdFailed() {
        doThrow(new EmptyResultDataAccessException(1)).when(resourceTemplateRepository).deleteById(resourceTemplate.getId());
        resourceTemplateService.deleteById(resourceTemplate.getId());
    }

    @Test
    public void testFindEntityById() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        assertEquals(resourceTemplate, resourceTemplateService.findEntityById(anyLong()));
    }

    @Test(expected = NotFoundException.class)
    public void testFindEntityByIdFail() {
        resourceTemplateService.findEntityById(null);
    }

    @Test
    public void testSearchByNameOrDescription() {
        List<ResourceTemplate> resourceTemplates = Collections.singletonList(resourceTemplate);
        when(resourceTemplateRepository.findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase
                (anyString(), anyString())).thenReturn(resourceTemplates);
        List<ResourceTemplateDTO> resourceTemplateDTOs = Collections.singletonList(resourceTempDTO);
        String searchedWord = "name";
        assertEquals(resourceTemplateDTOs, resourceTemplateService.searchByNameOrDescriptionContaining(searchedWord));
    }

    @Test
    public void testSelectPublishOrUnPublishTrue() throws Exception {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        map = new HashMap<>();
        map.put("isPublished", true);
        PowerMockito.doNothing().when(resourceTemplateService, "publishResourceTemplate", Mockito.any(ResourceTemplate.class));
        resourceTemplateService.selectPublishOrCancelPublishAction(resourceTemplate.getId(), map);
        verify(resourceTemplateRepository, times(1)).findById(resourceTemplate.getId());
        verify(resourceTemplateService, times(1)).selectPublishOrCancelPublishAction(resourceTemplate.getId(), map);
    }

    @Test
    public void testSelectPublishOrUnPublishFalse() throws Exception {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        map = new HashMap<>();
        map.put("isPublished", false);
        PowerMockito.doNothing().when(resourceTemplateService, "unPublishResourceTemplate", Mockito.any(ResourceTemplate.class));
        resourceTemplateService.selectPublishOrCancelPublishAction(resourceTemplate.getId(), map);
        verify(resourceTemplateRepository, times(1)).findById(resourceTemplate.getId());
        verify(resourceTemplateService, times(1)).selectPublishOrCancelPublishAction(resourceTemplate.getId(), map);
    }

    @Test
    public void testPublishResourceTemplateSuccess() throws Exception {
        PowerMockito.doReturn(true).when(resourceTemplateService,
                "verifyIfResourceTemplateHasParameters", Mockito.any(ResourceTemplate.class));
        PowerMockito.doReturn(true).when(resourceTemplateService,
                "verifyIfResourceTemplateIsNotPublished", Mockito.any(ResourceTemplate.class));
        PowerMockito.doNothing().when(
                jooqDDL, "createResourceContainerTable", Mockito.any(ResourceTemplate.class));
        Whitebox.invokeMethod(resourceTemplateService, "publishResourceTemplate", resourceTemplate);
        verifyPrivate(resourceTemplateService, times(1)).
                invoke("publishResourceTemplate", Mockito.any(ResourceTemplate.class));
        verifyPrivate(jooqDDL, times(1)).
                invoke("createResourceContainerTable", Mockito.any(ResourceTemplate.class));
    }

    @Test
    public void testPublishResourceTemplateFalse() throws Exception {
        PowerMockito.doReturn(false).when(resourceTemplateService,
                "verifyIfResourceTemplateHasParameters", Mockito.any(ResourceTemplate.class));
        PowerMockito.doReturn(true).when(resourceTemplateService,
                "verifyIfResourceTemplateIsNotPublished", Mockito.any(ResourceTemplate.class));
        PowerMockito.doNothing().when(
                jooqDDL, "createResourceContainerTable", Mockito.any(ResourceTemplate.class));
        Whitebox.invokeMethod(resourceTemplateService, "publishResourceTemplate", resourceTemplate);
        verifyPrivate(resourceTemplateService, times(1)).
                invoke("publishResourceTemplate", Mockito.any(ResourceTemplate.class));
        verifyPrivate(jooqDDL, times(0)).
                invoke("createResourceContainerTable", Mockito.any(ResourceTemplate.class));
    }

    @Test(expected = ResourceTemplateIsPublishedException.class)
    public void testPublishResourceTemplateFail() throws Exception {
        PowerMockito.doReturn(true).when(resourceTemplateService,
                "verifyIfResourceTemplateHasParameters", Mockito.any(ResourceTemplate.class));
        PowerMockito.doThrow(new ResourceTemplateIsPublishedException("dd")).when(resourceTemplateService,
                "verifyIfResourceTemplateIsNotPublished", Mockito.any(ResourceTemplate.class));
        PowerMockito.doNothing().when(
                jooqDDL, "createResourceContainerTable", Mockito.any(ResourceTemplate.class));
        try {
            Whitebox.invokeMethod(resourceTemplateService, "publishResourceTemplate", resourceTemplate);
        } catch (InvocationTargetException e) {
            e.getTargetException();
            e.getStackTrace();
        }
        verifyPrivate(resourceTemplateService, times(1)).
                invoke("publishResourceTemplate", Mockito.any(ResourceTemplate.class));
        verifyPrivate(jooqDDL, times(0)).
                invoke("createResourceContainerTable", Mockito.any(ResourceTemplate.class));
    }

    @Test
    public void testUnPublishResourceTemplateSuccess() throws Exception {
        PowerMockito.doReturn(true).when(resourceTemplateService,
                "verifyIfResourceTableCanBeDropped", Mockito.any(ResourceTemplate.class));
        PowerMockito.doReturn(true).when(resourceTemplateService,
                "verifyIfResourceTemplateIsPublished", Mockito.any(ResourceTemplate.class));
        PowerMockito.doNothing().when(
                jooqDDL, "dropResourceContainerTable", Mockito.any(ResourceTemplate.class));
        Whitebox.invokeMethod(resourceTemplateService, "unPublishResourceTemplate", resourceTemplate);
        verifyPrivate(resourceTemplateService, times(1)).
                invoke("unPublishResourceTemplate", Mockito.any(ResourceTemplate.class));
        verifyPrivate(jooqDDL, times(1)).
                invoke("dropResourceContainerTable", Mockito.any(ResourceTemplate.class));
    }

    @Test
    public void testUnPublishResourceTemplateFalse() throws Exception {
        PowerMockito.doReturn(false).when(resourceTemplateService,
                "verifyIfResourceTableCanBeDropped", Mockito.any(ResourceTemplate.class));
        PowerMockito.doReturn(true).when(resourceTemplateService,
                "verifyIfResourceTemplateIsPublished", Mockito.any(ResourceTemplate.class));
        Whitebox.invokeMethod(resourceTemplateService, "unPublishResourceTemplate", resourceTemplate);
        verifyPrivate(resourceTemplateService, times(1)).
                invoke("unPublishResourceTemplate", Mockito.any(ResourceTemplate.class));
        verifyPrivate(jooqDDL, times(0)).
                invoke("dropResourceContainerTable", Mockito.any(ResourceTemplate.class));
    }

    @Test
    public void testUnPublishResourceTemplateFail() throws Exception {
        PowerMockito.doReturn(true).when(resourceTemplateService,
                "verifyIfResourceTableCanBeDropped", Mockito.any(ResourceTemplate.class));
        PowerMockito.doReturn(false).when(resourceTemplateService,
                "verifyIfResourceTemplateIsPublished", Mockito.any(ResourceTemplate.class));
        Whitebox.invokeMethod(resourceTemplateService, "unPublishResourceTemplate", resourceTemplate);
        verifyPrivate(resourceTemplateService, times(1)).
                invoke("unPublishResourceTemplate", Mockito.any(ResourceTemplate.class));
        verifyPrivate(jooqDDL, times(0)).
                invoke("dropResourceContainerTable", Mockito.any(ResourceTemplate.class));
    }

    @Test
    public void testIfResourceTableCanBeDroppedSuccess() throws Exception {
        PowerMockito.doReturn(0).when(jooqDDL,
                "countTableRecords", Mockito.any(ResourceTemplate.class));
        PowerMockito.doReturn(false).when(jooqDDL,
                "countReferencesToTable", Mockito.any(ResourceTemplate.class));
        Boolean result = Whitebox.invokeMethod(resourceTemplateService,
                "verifyIfResourceTableCanBeDropped", resourceTemplate);
        assertTrue(result);
    }

    @Test(expected = ResourceTemplateCanNotBeUnPublished.class)
    public void testIfResourceTableCanNotBeDroppedFail() throws Exception {
        PowerMockito.doReturn(1).when(jooqDDL,
                "countTableRecords", Mockito.any(ResourceTemplate.class));
        PowerMockito.doReturn(false).when(jooqDDL,
                "countReferencesToTable", Mockito.any(ResourceTemplate.class));
        Whitebox.invokeMethod(resourceTemplateService,
                "verifyIfResourceTableCanBeDropped", resourceTemplate);
    }

    @Test(expected = ResourceTemplateCanNotBeUnPublished.class)
    public void testIfResourceTableCanNotBeDeletedFailed() throws Exception {
        PowerMockito.doReturn(0).when(jooqDDL,
                "countTableRecords", Mockito.any(ResourceTemplate.class));
        PowerMockito.doReturn(true).when(jooqDDL,
                "countReferencesToTable", Mockito.any(ResourceTemplate.class));
        Boolean result = Whitebox.invokeMethod(resourceTemplateService,
                "verifyIfResourceTableCanBeDropped", resourceTemplate);
        assertTrue(result);
    }

    @Test
    public void testVerificationOfResourceTemplateName() throws Exception {
        String name = "name";
        when(resourceTemplateRepository.findByName(name)).thenReturn(Optional.empty());
        String result = Whitebox.invokeMethod(resourceTemplateService, "verifyIfResourceTemplateNameIsUnique", name);
        assertEquals(name, result);
    }

    @Test(expected = NotUniqueNameException.class)
    public void testVerificationOfResourceTemplateNameFail() throws Exception {
        String name = "name";
        when(resourceTemplateRepository.findByName(name)).thenReturn(Optional.of(resourceTemplate));
        Whitebox.invokeMethod(resourceTemplateService, "verifyIfResourceTemplateNameIsUnique", name);
    }

    @Test
    public void testVerificationOfTemplateTableName() throws Exception {
        String name = "new name";
        String tableName = "new_name";
        when(resourceTemplateRepository.findByTableName(tableName)).thenReturn(Optional.empty());
        String result = Whitebox.invokeMethod(resourceTemplateService, "verifyIfResourceTemplateTableNameIsUnique", name);
        assertEquals(tableName, result);
    }

    @Test(expected = NotUniqueNameException.class)
    public void testVerificationOfTemplateTableNameFail() throws Exception {
        String name = "new name";
        String tableName = "new_name";
        when(resourceTemplateRepository.findByTableName(tableName)).thenReturn(Optional.of(resourceTemplate));
        String result = Whitebox.invokeMethod(resourceTemplateService, "verifyIfResourceTemplateTableNameIsUnique", name);
    }

    @Test
    public void testVerificationOfResourceTemplatePublish() throws Exception {
        resourceTemplate.setIsPublished(false);
        Boolean result = Whitebox.invokeMethod(resourceTemplateService, "verifyIfResourceTemplateIsNotPublished",
                resourceTemplate);
        assertTrue(result);
    }

    @Test(expected = ResourceTemplateIsPublishedException.class)
    public void testVerificationOfResourceTemplatePublishFail() throws Exception {
        resourceTemplate.setIsPublished(true);
        Boolean result = Whitebox.invokeMethod(resourceTemplateService, "verifyIfResourceTemplateIsNotPublished",
                resourceTemplate);
        assertFalse(result);
    }

    @Test
    public void testVerificationOfResourceTemplatePublishTrue() throws Exception {
        resourceTemplate.setIsPublished(true);
        Boolean result = Whitebox.invokeMethod(resourceTemplateService, "verifyIfResourceTemplateIsPublished",
                resourceTemplate);
        assertTrue(result);
    }

    @Test(expected = ResourceTemplateIsNotPublishedException.class)
    public void testVerificationOfResourceTemplatePublishFalse() throws Exception {
        resourceTemplate.setIsPublished(false);
        Boolean result = Whitebox.invokeMethod(resourceTemplateService, "verifyIfResourceTemplateIsPublished",
                resourceTemplate);
        assertTrue(result);
    }

    @Test(expected = ResourceTemplateParameterListIsEmpty.class)
    public void testVerificationOfResourceTemplateHavingParametersFail() throws Exception {
        resourceTemplate.setResourceParameters(Collections.emptyList());
        Whitebox.invokeMethod(resourceTemplateService, "verifyIfResourceTemplateHasParameters", resourceTemplate);
    }

    @Test
    public void testVerificationOfResourceTemplateHavingParameters() throws Exception {
        resourceTemplate.setResourceParameters(Collections.singletonList(new ResourceParameter(null, "name",
                "name", ParameterType.COORDINATES, null, resourceTemplate, null)));
        Boolean result = Whitebox.invokeMethod(resourceTemplateService, "verifyIfResourceTemplateHasParameters",
                resourceTemplate);
        assertTrue(result);
    }

    @Test(expected = NotFoundException.class)
    public void testFindByName() {
        when(resourceTemplateRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        resourceTemplateService.findByTableName(resourceTemplate.getName());
    }

    @Test
    public void testFindByNameEmpty() {
        when(resourceTemplateRepository.findByTableName(anyString())).thenReturn(Optional.of(resourceTemplate));
        resourceTemplateService.findByTableName(resourceTemplate.getTableName());
    }

    @Test
    public void addPermissionToResourceTemplateSuccess() throws Exception {
        doNothing().when(permissionManagerService)
                .addPermission(any(PermissionDto.class), any(Principal.class), any(Class.class));
        doReturn(resourceTemplate).when(resourceTemplateService1).findEntityById(anyLong());
        PowerMockito.doNothing().when(resourceTemplateService1, "sendNotification", anyBoolean(), anyString(), anyString());
        resourceTemplateService1.addPermissionToResourceTemplate(new PermissionDto(), principal);
    }

    @Test(expected = PermissionException.class)
    public void addPermissionToResourceTemplateFail() {
        doThrow(new PermissionException(ErrorMessage.ACCESS_DENIED.getMessage())).when(permissionManagerService)
                .addPermission(any(PermissionDto.class), any(Principal.class), any(Class.class));
        resourceTemplateService.addPermissionToResourceTemplate(new PermissionDto(), principal);
    }

    @Test(expected = PermissionException.class)
    public void changeOwnerToResourceTemplateSuccess() {
        doThrow(new PermissionException(ErrorMessage.ACCESS_DENIED.getMessage())).when(permissionManagerService)
                .changeOwner(any(ChangeOwnerDto.class), any(Principal.class), any(Class.class));
        resourceTemplateService.changeOwnerForResourceTemplate(new ChangeOwnerDto(), principal);
    }

    @Test(expected = PermissionException.class)
    public void changeOwnerToResourceTemplateFail() {
        doThrow(new PermissionException(ErrorMessage.ACCESS_DENIED.getMessage())).when(permissionManagerService)
                .changeOwner(any(ChangeOwnerDto.class), any(Principal.class), any(Class.class));
        resourceTemplateService.changeOwnerForResourceTemplate(new ChangeOwnerDto(), principal);
    }

    @Test
    public void closePermissionForCertainUserOk() throws Exception {
        doNothing().when(permissionManagerService)
                .closePermissionForCertainUser(any(PermissionDto.class), any(Principal.class), any(Class.class));
        doReturn(resourceTemplate).when(resourceTemplateService1).findEntityById(anyLong());
        PowerMockito.doNothing().when(resourceTemplateService1, "sendNotification", anyBoolean(), anyString(), anyString());
        resourceTemplateService1.closePermissionForCertainUser(new PermissionDto(), principal);
    }

    @Test(expected = PermissionException.class)
    public void closePermissionForCertainUserFailAccess() {
        doThrow(new PermissionException(ErrorMessage.ACCESS_DENIED.getMessage())).when(permissionManagerService)
                .closePermissionForCertainUser(any(PermissionDto.class), any(Principal.class), any(Class.class));
        resourceTemplateService.closePermissionForCertainUser(new PermissionDto(), principal);
    }
}
