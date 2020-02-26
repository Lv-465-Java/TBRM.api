package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.resourceparameter.ResourceParameterDTO;
import com.softserve.rms.dto.resourceparameter.ResourceParameterSaveDTO;
import com.softserve.rms.dto.resourceparameter.ResourceRelationDTO;
import com.softserve.rms.entities.ParameterType;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceRelation;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.NotDeletedException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.exceptions.resourceParameter.ResourceParameterCanNotBeModified;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateIsNotPublishedException;
import com.softserve.rms.repository.ResourceParameterRepository;
import com.softserve.rms.repository.ResourceRelationRepository;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.util.RangeIntegerPatternGenerator;
import com.softserve.rms.util.Validator;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourceParameterServiceImpl.class)
public class ResourceParameterServiceImplTest {

    @InjectMocks
    private ResourceParameterServiceImpl resourceParameterService;

    @Mock
    private ResourceTemplateService resourceTemplateService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ResourceParameterRepository resourceParameterRepository;

    @Mock
    private ResourceRelationRepository resourceRelationRepository;

    @Mock
    private Validator validator;

    @Mock
    private DSLContext dslContext;

    @Mock
    private RangeIntegerPatternGenerator patternGenerator;

    private ResourceTemplate resourceTemplate = new ResourceTemplate(1L, "template", "resource_template", "some description", false, null, null, null);
    private ResourceParameter resourceParameter = new ResourceParameter(1L, "resourceParameter", "resource_parameter", ParameterType.POINT_INT, "regex", resourceTemplate, null);
    private ResourceParameter resourceParameterUpdate = new ResourceParameter(1L, "resourceParameterUpdate", "resource_parameter_update", ParameterType.POINT_INT, "regex", resourceTemplate, null);
    private ResourceParameterDTO resourceParameterDTO = new ResourceParameterDTO(1L, "resourceParameter", "resource_parameter", ParameterType.POINT_INT, "regex", 1L, null);
    private ResourceParameterDTO resourceParameterDTOUpdate = new ResourceParameterDTO(1L, "resourceParameterUpdate", "resource_parameter_update", ParameterType.POINT_INT, "regex", 1L, null);
    private ResourceParameterSaveDTO resourceParameterSaveDTO = new ResourceParameterSaveDTO("resourceParameter", ParameterType.POINT_INT, "regex", null);
    private ResourceParameterSaveDTO resourceParameterSaveDTOUpdate = new ResourceParameterSaveDTO("resourceParameterUpdate", ParameterType.POINT_INT, "regex", null);
    private ResourceRelation resourceRelation = new ResourceRelation(1L, resourceParameter, resourceTemplate);
    private ResourceRelationDTO resourceRelationDTO = new ResourceRelationDTO(1L);
    private ResourceParameterDTO resourceParameterDTOWithRelation = new ResourceParameterDTO(1L, "resourceParameter", "resource_parameter", ParameterType.POINT_INT, "regex", 1L, resourceRelationDTO);
    private List<ResourceParameterDTO> parameterDTOS = Arrays.asList(
            new ResourceParameterDTO(1L, "firstParameter", "first_parameter", ParameterType.POINT_INT, null, 1L, null),
            new ResourceParameterDTO(2L, "secondParameter", "second_parameter", ParameterType.POINT_INT, null, 1L, null));

    private List<ResourceParameter> parameters = Arrays.asList(
            new ResourceParameter(1L, "firstParameter", "first_parameter", ParameterType.POINT_INT, null, resourceTemplate, null),
            new ResourceParameter(2L, "secondParameter", "second_parameter", ParameterType.POINT_INT, null, resourceTemplate, null));

    @Before
    public void initializeMock() {
        resourceParameterService = PowerMockito.spy(new ResourceParameterServiceImpl(resourceParameterRepository,
                resourceTemplateService, resourceRelationRepository, dslContext));
        ResourceRelationRepository resourceRelationRepository = PowerMockito.mock(ResourceRelationRepository.class);

    }

    @Test
    public void getResourceParametersByTemplateId() {
        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
        when(resourceParameterRepository.findAllByResourceTemplateId(anyLong())).thenReturn(parameters);
        assertEquals(parameterDTOS, resourceParameterService.findAllByTemplateId(anyLong()));
    }

    @Test
    public void getResourceParameterByIdSuccess() {
        when(resourceParameterRepository.findById(anyLong())).thenReturn(Optional.of(resourceParameter));
        assertEquals(resourceParameter, resourceParameterService.findById(resourceParameter.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void getResourceParameterByIdFailed() {
        resourceParameterService.findById(anyLong());
    }

    @Test
    public void getResourceParameterByIdDTOSuccess() {
        when(resourceParameterRepository.findById(anyLong())).thenReturn(Optional.of(resourceParameter));
        assertEquals(resourceParameterDTO, resourceParameterService.findByIdDTO(resourceTemplate.getId(), resourceParameter.getId()));
    }

    @Test(expected = NotFoundException.class)
    public void getResourceParameterDTOByIdFailed() {
        resourceParameterService.findByIdDTO(null, null);
    }

    @Test
    public void checkIfParameterCanBeAddedSuccess() throws Exception {
        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
        PowerMockito.doReturn(resourceParameterDTO).when(resourceParameterService, "save", anyLong(), any(ResourceParameterSaveDTO.class));
        assertEquals(resourceParameterService.checkIfParameterCanBeSaved(resourceTemplate.getId(), resourceParameterSaveDTOUpdate), resourceParameterDTO);
    }

    @Test(expected = ResourceParameterCanNotBeModified.class)
    public void checkIfParameterCanBeAddedFailed() {
        resourceTemplate.setIsPublished(true);
        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
        resourceParameterService.checkIfParameterCanBeSaved(resourceTemplate.getId(), resourceParameterSaveDTO);
    }

    @Test
    public void saveResourceParameterSuccess() throws Exception {
        PowerMockito.doReturn("resourceParameter").when(resourceParameterService, "verifyIfParameterNameIsUniquePerResourceTemplate", anyString(), anyLong());
        PowerMockito.doReturn("resource_parameter").when(resourceParameterService, "verifyIfParameterColumnNameIsUniquePerResourceTemplate", anyString(), anyLong());
        PowerMockito.doReturn("regex").when(resourceParameterService, "getMatchedPatternToParameterType", any(ParameterType.class), anyString());
        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
        ResourceParameterDTO actual = resourceParameterService.save(resourceTemplate.getId(), resourceParameterSaveDTO);
        actual.setId(1L);
        assertEquals(resourceParameterDTO, actual);
    }

    @Test
    public void checkIfParameterCanBeUpdatedSuccess() throws Exception {
        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
        doReturn(resourceParameter).when(resourceParameterService).findById(anyLong());
        PowerMockito.doReturn(resourceParameterDTOUpdate).when(resourceParameterService, "updateById", anyLong(), any(ResourceParameter.class), any(ResourceParameterSaveDTO.class));
        assertEquals(resourceParameterService.checkIfParameterCanBeUpdated(resourceTemplate.getId(), resourceParameter.getId(), resourceParameterSaveDTOUpdate), resourceParameterDTOUpdate);
    }

    @Test(expected = ResourceParameterCanNotBeModified.class)
    public void checkIfParameterCanBeUpdatedFailed() {
        resourceTemplate.setIsPublished(true);
        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
        resourceParameterService.checkIfParameterCanBeUpdated(resourceTemplate.getId(), resourceParameter.getId(), resourceParameterSaveDTOUpdate);
    }

    @Test
    public void updateById() throws Exception {
        PowerMockito.doNothing().when(resourceParameterService, "updateParameterNameAndColumnName", anyLong(), any(ResourceParameter.class), any(ResourceParameterSaveDTO.class));
        PowerMockito.doReturn("regex").when(resourceParameterService, "getMatchedPatternToParameterType", any(ParameterType.class), anyString());
        PowerMockito.doReturn(resourceRelation).when(resourceParameterService, "saveParameterRelation", anyLong(), any(ResourceRelationDTO.class));
        ResourceParameterDTO actual = Whitebox.invokeMethod(resourceParameterService, "updateById", resourceTemplate.getId(), resourceParameterUpdate, resourceParameterSaveDTOUpdate);
        assertEquals(resourceParameterDTOUpdate, actual);
    }

    @Test
    public void saveParameterRelationSuccess() throws Exception {
        doReturn(resourceParameter).when(resourceParameterService).findById(anyLong());
        doReturn(resourceTemplate).when(resourceTemplateService).findEntityById(anyLong());
        PowerMockito.doReturn(resourceTemplate).when(resourceParameterService, "verifyIfResourceTemplateIsPublished", any(ResourceTemplate.class));
        when(resourceRelationRepository.save(any(ResourceRelation.class))).thenReturn(resourceRelation);
        ResourceRelation relation = Whitebox.invokeMethod(resourceParameterService, "saveParameterRelation", resourceParameter.getId(), resourceRelationDTO);
        assertEquals(resourceRelation, resourceRelationRepository.save(relation));
    }


    @Test(expected = NotUniqueNameException.class)
    public void verifyIfParameterColumnNameIsUniquePerResourceTemplateFailed() throws Exception {
        String columnName = "resource_parameter";
        when(validator.generateTableOrColumnName(anyString())).thenReturn(columnName);
        when(resourceParameterRepository.findByColumnNameAndResourceTemplateId(anyString(), anyLong())).thenReturn(Optional.of(resourceParameter));
        Whitebox.invokeMethod(resourceParameterService, "verifyIfParameterColumnNameIsUniquePerResourceTemplate", anyString(), anyLong());
    }

    @Test
    public void verifyIfParameterColumnNameIsUniquePerResourceTemplateSuccess() throws Exception {
        String columnName = "resource_parameter";
        when(validator.generateTableOrColumnName(anyString())).thenReturn(columnName);
        when(resourceParameterRepository.findByColumnNameAndResourceTemplateId(anyString(), anyLong())).thenReturn(Optional.empty());
        String actual = Whitebox.invokeMethod(resourceParameterService, "verifyIfParameterColumnNameIsUniquePerResourceTemplate", columnName, resourceTemplate.getId());
        assertEquals(columnName, actual);
    }

    @Test
    public void testIfParameterCanBeDeletedTrue() {
        when(resourceTemplateService.findEntityById(any(Long.class))).thenReturn(resourceTemplate);
        resourceParameterService.checkIfParameterCanBeDeleted(resourceTemplate.getId(), resourceParameter.getId());
        verify(resourceParameterRepository, times(1)).deleteById(anyLong());
    }

    @Test(expected = ResourceParameterCanNotBeModified.class)
    public void testIfParameterCanBeDeletedFalse() {
        when(resourceTemplateService.findEntityById(any(Long.class))).thenReturn(resourceTemplate);
        resourceTemplate.setIsPublished(true);
        resourceParameterService.checkIfParameterCanBeDeleted(resourceTemplate.getId(), resourceParameter.getId());
        verify(resourceParameterRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void deleteResourceParameterSuccess() {
        resourceParameterRepository.deleteById(anyLong());
        verify(resourceParameterRepository, times(1)).deleteById(anyLong());
    }

    @Test(expected = NotDeletedException.class)
    public void deleteResourceParameterFail() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(resourceParameterRepository).deleteById(anyLong());
        Whitebox.invokeMethod(resourceParameterService, "deleteById", anyLong());
        verifyPrivate(resourceParameterService, times(1)).
                invoke("deleteById", any(Long.class));
    }

    @Test
    public void testUpdateOfParameterNameOrColumnName() throws Exception {
        PowerMockito.doReturn("resource parameter").when(resourceParameterService,
                "verifyIfParameterNameIsUniquePerResourceTemplate",
                Mockito.any(String.class), Mockito.any(Long.class));
        PowerMockito.doReturn("resource_parameter").when(resourceParameterService,
                "verifyIfParameterColumnNameIsUniquePerResourceTemplate",
                Mockito.any(String.class), Mockito.any(Long.class));
        Whitebox.invokeMethod(resourceParameterService, "updateParameterNameAndColumnName",
                resourceTemplate.getId(), resourceParameter, resourceParameterSaveDTO);
        verifyPrivate(resourceParameterService, times(1)).
                invoke("updateParameterNameAndColumnName", Mockito.any(Long.class),
                        Mockito.any(ResourceParameter.class), Mockito.any(ResourceParameterSaveDTO.class));
    }

    @Test
    public void testUpdateOfParameterNameOrColumnNameFail() throws Exception {
        resourceParameter.setName("resource_parameter");
        Whitebox.invokeMethod(resourceParameterService, "updateParameterNameAndColumnName",
                resourceTemplate.getId(), resourceParameter, resourceParameterSaveDTO);
        verifyPrivate(resourceParameterService, times(1)).
                invoke("updateParameterNameAndColumnName", Mockito.any(Long.class),
                        Mockito.any(ResourceParameter.class), Mockito.any(ResourceParameterSaveDTO.class));
        verifyPrivate(resourceParameterService, times(1)).
                invoke("verifyIfParameterNameIsUniquePerResourceTemplate", Mockito.any(String.class),
                        Mockito.any(Long.class));
        verifyPrivate(resourceParameterService, times(1)).
                invoke("verifyIfParameterColumnNameIsUniquePerResourceTemplate", Mockito.any(String.class),
                        Mockito.any(Long.class));
    }

    @Test
    public void testVerificationOfResourceTemplatePublishTrue() throws Exception {
        resourceTemplate.setIsPublished(true);
        ResourceTemplate result = Whitebox.invokeMethod(resourceParameterService,
                "verifyIfResourceTemplateIsPublished", resourceTemplate);
        assertEquals(resourceTemplate, result);
    }

    @Test(expected = ResourceTemplateIsNotPublishedException.class)
    public void testVerificationOfResourceTemplatePublishFail() throws Exception {
        resourceTemplate.setIsPublished(false);
        Whitebox.invokeMethod(resourceParameterService,
                "verifyIfResourceTemplateIsPublished", resourceTemplate);
    }

    @Test
    public void testVerificationOfParameterNameUniquenessTrue() throws Exception {
        when(resourceParameterRepository.findByNameAndResourceTemplateId(Mockito.any(String.class),
                Mockito.any(Long.class))).thenReturn(Optional.empty());
        String result = Whitebox.invokeMethod(resourceParameterService,
                "verifyIfParameterNameIsUniquePerResourceTemplate", resourceParameter.getName(),
                resourceParameter.getId());
        assertEquals(resourceParameter.getName(), result);
    }

    @Test(expected = NotUniqueNameException.class)
    public void testVerificationOfParameterNameUniquenessFail() throws Exception {
        when(resourceParameterRepository.findByNameAndResourceTemplateId(Mockito.any(String.class),
                Mockito.any(Long.class))).thenReturn(Optional.of(resourceParameter));
        String result = Whitebox.invokeMethod(resourceParameterService,
                "verifyIfParameterNameIsUniquePerResourceTemplate", resourceParameter.getName(),
                resourceParameter.getId());
    }

    @Test
    public void testUpdateParameterRelationTrue() throws Exception {
        Mockito.doReturn(resourceRelation).when(resourceRelationRepository)
                .findByResourceParameterId(Mockito.any(Long.class));
        when(resourceTemplateService.findEntityById(Mockito.any(Long.class))).thenReturn(resourceTemplate);
        PowerMockito.doReturn(resourceTemplate).when(resourceParameterService,
                "verifyIfResourceTemplateIsPublished", resourceTemplate);
        ResourceRelation result = Whitebox.invokeMethod(resourceParameterService,
                "updateParameterRelation", resourceParameter.getId(), resourceRelationDTO);
        verifyPrivate(resourceParameterService, times(1)).
                invoke("verifyIfResourceTemplateIsPublished", Mockito.any(ResourceTemplate.class));
        assertEquals(resourceRelation, result);
    }

    @Test
    public void testUpdateParameterRelationFalse() throws Exception {
        Mockito.doReturn(null).when(resourceRelationRepository)
                .findByResourceParameterId(Mockito.any(Long.class));
        PowerMockito.doReturn(resourceRelation).when(resourceParameterService,
                "saveParameterRelation", Mockito.any(Long.class), Mockito.any(ResourceRelationDTO.class));
        Whitebox.invokeMethod(resourceParameterService,
                "updateParameterRelation", resourceParameter.getId(), resourceRelationDTO);
        verifyPrivate(resourceParameterService, times(0)).
                invoke("verifyIfResourceTemplateIsPublished", Mockito.any(ResourceTemplate.class));
    }
}