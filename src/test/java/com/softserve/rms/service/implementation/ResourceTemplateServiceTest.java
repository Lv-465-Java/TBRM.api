package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.entities.*;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.exceptions.resourseTemplate.*;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.repository.implementation.JooqDDL;
import com.softserve.rms.util.Validator;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
//@PrepareForTest(JooqDDL.class)
public class ResourceTemplateServiceTest {
    @InjectMocks
    private ResourceTemplateServiceImpl resourceTemplateService;
    @Mock
    private ModelMapper modelMapper = new ModelMapper();
    @Mock
    private Validator validator = new Validator();
    @Mock
    private ResourceTemplateRepository resourceTemplateRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private PermissionManagerServiceImpl permissionManagerService;
    @Mock
    private DSLContext dslContext;
    @Mock
    Authentication authentication;
    @Mock
    Principal principal;
    @Mock
    SecurityContext securityContext;
    @Mock
    private JooqDDL jooqDDL;
//    private JooqDDL jooqDDL = PowerMockito.mock(JooqDDL.class);

    @Mock
    private ResourceTemplateServiceImpl mockMock;


    private ResourceTemplateServiceImpl mocks;

    private Role role = new Role(2L, "MANAGER");
    private User user = new User(1L, "testName", "testSurname", "testEmail", "any",
            "any", false, role, Collections.emptyList());
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
        mocks = PowerMockito.spy(new ResourceTemplateServiceImpl(resourceTemplateRepository, userService,
                permissionManagerService, dslContext));
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
    }

    //    @Test
//    public void testSaveResourceTemplate() {
//
//
//        SecurityContextHolder.setContext(securityContext);
//        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
//        Principal mockPrincipal = Mockito.mock(Principal.class);
//        Mockito.when(mockPrincipal.getName()).thenReturn(user.getEmail());
//        when(userService.getUserByEmail(anyString())).thenReturn(user);
//        when(resourceTemplateRepository.saveAndFlush(any())).thenReturn(resourceTemplate);
//        assertEquals(resourceTemplateDTO, resourceTemplateService.save(resourceTemplateSaveDTO));
//    }
//
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
    public void testFindAllByUserId() {
        when(resourceTemplateRepository.findAllByUserId(anyLong())).thenReturn(Collections.singletonList(resourceTemplate));
        List<ResourceTemplateDTO> resourceTemplateDTOs = Collections.singletonList(resourceTempDTO);
        assertEquals(resourceTemplateDTOs, resourceTemplateService.getAllByUserId(anyLong()));
    }

    @Test(expected = ResourceTemplateCanNotBeModified.class)
    public void testUpdateResourceTemplateWithPublishFalse() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        resourceTemplate.setIsPublished(true);
        ResourceTemplateDTO resultResourceTemplate = resourceTemplateService.updateById(anyLong(), map);
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
        ResourceTemplateDTO temDto = resourceTemplateService.updateById(anyLong(), map);
        assertEquals(updatedDTO, temDto);
    }

    @Test
    public void testUpdateResourceTemplateWithDescription() throws Exception {
        ResourceTemplateDTO updatedDTO = new ResourceTemplateDTO(1L, "name", "name",
                "updated description", false, user.getId(), Collections.emptyList());
        map = new HashMap<>();
        map.put("description", "updated description");
        ResourceTemplateDTO resultResourceTemplate = Whitebox.invokeMethod(
                mocks, "updateResourceTemplateFields", resourceTemplate, map);
        assertEquals(updatedDTO, resultResourceTemplate);
    }

    @Test
    public void testUpdateResourceTemplateWithName() throws Exception {
        ResourceTemplateDTO updatedDTO = new ResourceTemplateDTO(1L, "updated name", "updated_name",
                "description", false, user.getId(), Collections.emptyList());
        map = new HashMap<>();
        map.put("name", "updated name");
        ResourceTemplateDTO resultResourceTemplate = Whitebox.invokeMethod(
                mocks, "updateResourceTemplateFields", resourceTemplate, map);
        assertEquals(updatedDTO, resultResourceTemplate);
    }

    @Test
    public void testDeleteByIdTrue() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        resourceTemplateService.deleteById(resourceTemplate.getId());
        verify(resourceTemplateRepository, times(1)).deleteById(resourceTemplate.getId());
    }

    @Test(expected = ResourceTemplateCanNotBeModified.class)
    public void testDeleteByIdFalse() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        resourceTemplate.setIsPublished(true);
        verify(resourceTemplateRepository, times(0)).deleteById(resourceTemplate.getId());
        resourceTemplateService.deleteById(resourceTemplate.getId());
    }

    @Test
    public void testDeleteById() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        doNothing().when(permissionManagerService).closeAllPermissionsToResource(anyLong(), any());
        resourceTemplateService.delete(resourceTemplate.getId());
        verify(resourceTemplateRepository, times(1)).deleteById(resourceTemplate.getId());
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteByIdFailed() {
        doThrow(new EmptyResultDataAccessException(1)).when(resourceTemplateRepository).deleteById(resourceTemplate.getId());
        resourceTemplateService.delete(resourceTemplate.getId());
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


//    @Test
//    public void testSelectPublishOrUnPublish() throws Exception {
//        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
//        map = new HashMap<>();
//        map.put("isPublished", true);
//        doNothing().when(mockMock).publishResourceTemplate(any());
//        doNothing().when(Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateHasParameters", resourceTemplate));
//        resourceTemplateService.selectPublishOrCancelPublishAction(resourceTemplate.getId(), map);
//        verify(mockMock, times(1)).selectPublishOrCancelPublishAction(resourceTemplate.getId(), map);
//        verify(mockMock, times(1)).publishResourceTemplate(resourceTemplate);
//    }

//    @Override
//    @Transactional
//    public void selectPublishOrCancelPublishAction(Long id, Map<String, Object> body) {
//        ResourceTemplate resourceTemplate = findEntityById(id);
//        if (body.get(FieldConstants.IS_PUBLISHED.getValue()).equals(true)) {
//            publishResourceTemplate(resourceTemplate);
//        } else {
//            unPublishResourceTemplate(resourceTemplate);
//        }
//    }

    @Test
    public void testVerificationOfResourceTemplateName() throws Exception {
        String name = "name";
        when(resourceTemplateRepository.findByName(name)).thenReturn(Optional.empty());
        String result = Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateNameIsUnique", name);
        assertEquals(name, result);
    }

    @Test(expected = NotUniqueNameException.class)
    public void testVerificationOfResourceTemplateNameFail() throws Exception {
        String name = "name";
        when(resourceTemplateRepository.findByName(name)).thenReturn(Optional.of(resourceTemplate));
        Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateNameIsUnique", name);
    }

    @Test
    public void testVerificationOfTemplateTableName() throws Exception {
        String name = "new name";
        String tableName = "new_name";
        when(resourceTemplateRepository.findByTableName(tableName)).thenReturn(Optional.empty());
        String result = Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateTableNameIsUnique", name);
        assertEquals(tableName, result);
    }

    @Test(expected = NotUniqueNameException.class)
    public void testVerificationOfTemplateTableNameFail() throws Exception {
        String name = "new name";
        String tableName = "new_name";
        when(resourceTemplateRepository.findByTableName(tableName)).thenReturn(Optional.of(resourceTemplate));
        String result = Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateTableNameIsUnique", name);
    }

    @Test
    public void testVerificationOfResourceTemplatePublish() throws Exception {
        resourceTemplate.setIsPublished(false);
        Boolean result = Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateIsNotPublished",
                resourceTemplate);
        assertTrue(result);
    }

    @Test(expected = ResourceTemplateIsPublishedException.class)
    public void testVerificationOfResourceTemplatePublishFail() throws Exception {
        resourceTemplate.setIsPublished(true);
        Boolean result = Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateIsNotPublished",
                resourceTemplate);
        assertFalse(result);
    }

    @Test
    public void testVerificationOfResourceTemplatePublishTrue() throws Exception {
        resourceTemplate.setIsPublished(true);
        Boolean result = Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateIsPublished",
                resourceTemplate);
        assertTrue(result);
    }

    @Test(expected = ResourceTemplateIsNotPublishedException.class)
    public void testVerificationOfResourceTemplatePublishFalse() throws Exception {
        resourceTemplate.setIsPublished(false);
        Boolean result = Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateIsPublished",
                resourceTemplate);
        assertTrue(result);
    }

    @Test(expected = ResourceTemplateParameterListIsEmpty.class)
    public void testVerificationOfResourceTemplateHavingParametersFail() throws Exception {
        resourceTemplate.setResourceParameters(Collections.emptyList());
        Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateHasParameters", resourceTemplate);
    }

    @Test
    public void testVerificationOfResourceTemplateHavingParameters() throws Exception {
        resourceTemplate.setResourceParameters(Collections.singletonList(new ResourceParameter(null, "name",
                "name", ParameterType.COORDINATES, null, resourceTemplate, null)));
        Boolean result = Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateHasParameters",
                resourceTemplate);
        assertTrue(result);
    }

//    @Test
//    public void testIfResourceTableIsEmptyTrue() {
//        when(jooqDDL.countTableRecords(resourceTemplate)).thenReturn(0);
//        Boolean result = resourceTemplateService.verifyIfResourceTableIsEmpty(resourceTemplate);
//        assertTrue(result);
//    }

//    public Boolean verifyIfResourceTableIsEmpty(ResourceTemplate resourceTemplate) {
//        if (jooqDDL.countTableRecords(resourceTemplate) > 0) {
//            throw new ResourceTemplateCanNotBeUnPublished(
//                    ErrorMessage.RESOURCE_TEMPLATE_TABLE_CAN_NOT_BE_DROP.getMessage());
//        }
//        return true;
//    }

//    @Test
//    public void testPublishOfResourceTemplatePassed() throws Exception {
//        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
//        resourceTemplate.setResourceParameters(Collections.singletonList(new ResourceParameter(null, "name",
//                "name", ParameterType.COORDINATES, null, resourceTemplate, null)));
//  //      doNothing().when(dslContext).createTable(anyString());
//        whenNew(JooqDDL.class).withArguments(Mockito.any(DSLContext.class)).thenReturn(jooqDDL);
//
////        doNothing().when(jooqDDL).createResourceContainerTable(any(ResourceTemplate.class));
//        resourceTemplateService.publishResourceTemplate(resourceTemplate);
//        verify(mockMock, times(1)).publishResourceTemplate(resourceTemplate);
////        assertTrue(result);
//    }

//    @Test(expected = ResourceTemplateIsPublishedException.class)
//    public void testPublishOfResourceTemplatePublishedExceptionFailed() {
//        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
//        resourceTemplate.setIsPublished(true);
//        resourceTemplate.setResourceParameters(Collections.singletonList(new ResourceParameter(null, "name",
//                "name", ParameterType.AREA_DOUBLE, null, resourceTemplate, null)));
//        Boolean result = resourceTemplateService.publishResourceTemplate(resourceTemplate.getId());
//    }
//
//    @Test(expected = ResourceTemplateParameterListIsEmpty.class)
//    public void testPublishOfResourceTemplateParameterListIsEmptyFail() {
//        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
//        resourceTemplate.setResourceParameters(Collections.emptyList());
//        Boolean result = resourceTemplateService.publishResourceTemplate(resourceTemplate.getId());
//    }
//
//    @Test(expected = ResourceTemplateIsPublishedException.class)
//    public void testPublishOfResourceTemplateException() {
//        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
//        resourceTemplate.setIsPublished(true);
//        resourceTemplate.setResourceParameters(Collections.emptyList());
//        Boolean result = resourceTemplateService.publishResourceTemplate(resourceTemplate.getId());
//    }
//
//    @Test
//    public void testUnPublishOfResourceTemplateWithValueTrue() {
//        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
//        resourceTemplate.setIsPublished(true);
//        assertTrue(resourceTemplateService.unPublishResourceTemplate(resourceTemplate.getId()));
//    }
//
//    @Test
//    public void testUnPublishOfResourceTemplateWithValueFalse() {
//        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
//        assertTrue(resourceTemplateService.unPublishResourceTemplate(resourceTemplate.getId()));
//    }
}