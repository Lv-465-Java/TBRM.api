package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
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
import com.softserve.rms.repository.implementation.JooqDDL;
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
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private RangeIntegerPatternGenerator patternGenerator;
    @Mock
    private DSLContext dslContext;

    private ResourceTemplate resourceTemplate = new ResourceTemplate(1L, "template", "resource_template", "some description", false, null, null, null);
    private ResourceParameter resourceParameter = new ResourceParameter(1L, "resourceParameter", "resource_parameter", ParameterType.POINT_INT, null, null, null);
    private ResourceParameterDTO resourceParameterDTO = new ResourceParameterDTO(1L, "resourceParameter", "resource_parameter", ParameterType.POINT_INT, null, null, null);
    private ResourceRelationDTO resourceRelationDTO = new ResourceRelationDTO(1L);
    private ResourceParameterSaveDTO resourceParameterSaveDTO = new ResourceParameterSaveDTO("resource_parameter", ParameterType.POINT_INT, null, resourceRelationDTO);
    private ResourceRelation resourceRelation = new ResourceRelation(1L, resourceParameter, resourceTemplate);
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

//    @Test(expected = NotFoundException.class)
//    public void getResourceParameterByIdFailed() {
//        resourceParameterService.findById(anyLong());
//    }
//
//    @Test
//    public void getResourceParameterByIdDTOSuccess() {
//        when(resourceParameterRepository.findById(anyLong())).thenReturn(Optional.of(resourceParameter));
//        assertEquals(resourceParameterDTO, resourceParameterService.findByIdDTO(anyLong(), anyLong()));
//    }
//
//    @Test(expected = NotFoundException.class)
//    public void getResourceParameterDTOByIdFailed() {
//        resourceParameterService.findByIdDTO(anyLong(), anyLong());
//    }
//
//    //TODO
//    @Test
//    public void saveResourceParameterSuccess() {
//        when(resourceParameterRepository.save(resourceParameter)).thenReturn(resourceParameter);
//        assertEquals(resourceParameter, resourceParameterRepository.save(resourceParameter));
//    }
//
//    @Test
//    public void saveResourceRelationSuccess() {
//        when(resourceRelationRepository.save(resourceRelation)).thenReturn(resourceRelation);
//        assertEquals(resourceRelation, resourceRelationRepository.save(resourceRelation));
//    }


    @Test(expected = ResourceParameterCanNotBeModified.class)
    public void testIfParameterCanBeDeletedFalse() {
        resourceTemplate.setIsPublished(true);
        when(resourceTemplateService.findEntityById(any(Long.class))).thenReturn(resourceTemplate);
        resourceParameterService.checkIfParameterCanBeDeleted(resourceTemplate.getId(), resourceParameter.getId());
        verify(resourceParameterRepository, times(0)).deleteById(anyLong());
    }

    @Test
    public void testIfParameterCanBeDeletedTrue() {
        when(resourceTemplateService.findEntityById(any(Long.class))).thenReturn(resourceTemplate);
        resourceParameterService.checkIfParameterCanBeDeleted(resourceTemplate.getId(), resourceParameter.getId());
        verify(resourceParameterRepository, times(1)).deleteById(anyLong());
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
        verifyPrivate(resourceParameterService, times(0)).
                invoke("verifyIfParameterNameIsUniquePerResourceTemplate", Mockito.any(String.class),
                        Mockito.any(Long.class));
        verifyPrivate(resourceParameterService, times(0)).
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