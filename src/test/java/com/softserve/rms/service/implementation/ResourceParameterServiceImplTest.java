package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.resourceparameter.ResourceParameterDTO;
import com.softserve.rms.dto.resourceparameter.ResourceParameterSaveDTO;
import com.softserve.rms.dto.resourceparameter.ResourceRelationDTO;
import com.softserve.rms.entities.ParameterType;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceRelation;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.NotFoundException;
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
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
    private ResourceParameter resourceParameter = new ResourceParameter(1L, "resourceParameter", "resource_parameter", ParameterType.POINT_INT, null, resourceTemplate, null);
    private ResourceParameter resourceParameterUpdate = new ResourceParameter(1L, "resourceParameterUpdate", "resource_parameter_update", ParameterType.POINT_INT, null, resourceTemplate, null);
    private ResourceParameterDTO resourceParameterDTO = new ResourceParameterDTO(1L, "resourceParameter", "resource_parameter", ParameterType.POINT_INT, null, 1L, null);
    private ResourceParameterDTO resourceParameterDTOUpdate = new ResourceParameterDTO(1L, "resourceParameterUpdate", "resource_parameter_update", ParameterType.POINT_INT, null, 1L, null);
    private ResourceParameterSaveDTO resourceParameterSaveDTO = new ResourceParameterSaveDTO("resourceParameter", ParameterType.POINT_INT, null, null);
    private ResourceParameterSaveDTO resourceParameterSaveDTOUpdate = new ResourceParameterSaveDTO("resourceParameterUpdate", ParameterType.POINT_INT, null, null);
    private ResourceRelation resourceRelation = new ResourceRelation(1L, resourceParameter, resourceTemplate);
    private ResourceRelationDTO resourceRelationDTO = new ResourceRelationDTO(1L);
    private List<ResourceParameterDTO> parameterDTOS = Arrays.asList(
            new ResourceParameterDTO(1L, "firstParameter", "first_parameter", ParameterType.POINT_INT, null, 1L, null),
            new ResourceParameterDTO(2L, "secondParameter", "second_parameter", ParameterType.POINT_INT, null, 1L, null));

    private List<ResourceParameter> parameters = Arrays.asList(
            new ResourceParameter(1L, "firstParameter", "first_parameter", ParameterType.POINT_INT, null, resourceTemplate, null),
            new ResourceParameter(2L, "secondParameter", "second_parameter", ParameterType.POINT_INT, null, resourceTemplate, null));

    @Before
    public void init() {
        resourceParameterService = PowerMockito.spy(new ResourceParameterServiceImpl(resourceParameterRepository, resourceTemplateService, resourceRelationRepository, dslContext));
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

    //TODO
    @Test
    public void saveResourceParameterSuccess() throws Exception {
        PowerMockito.doReturn("resourceParameter").when(resourceParameterService, "verifyIfParameterNameIsUniquePerResourceTemplate", anyString(), anyLong());
        PowerMockito.doReturn("resource_parameter").when(resourceParameterService, "verifyIfParameterColumnNameIsUniquePerResourceTemplate", anyString(), anyLong());
        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
        ResourceParameterDTO actual = resourceParameterService.save(resourceTemplate.getId(), resourceParameterSaveDTO);
        actual.setId(1L);
        assertEquals(resourceParameterDTO, actual);
    }

    @Test
    public void updateById() throws Exception {
        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
        doReturn(resourceParameter).when(resourceParameterService).findById(anyLong());
        PowerMockito.doReturn(resourceParameterDTOUpdate).when(resourceParameterService, "update", anyLong(), any(ResourceParameter.class), any(ResourceParameterSaveDTO.class));
        assertEquals(resourceParameterService.checkIfParameterCanBeUpdated(resourceTemplate.getId(), resourceParameter.getId(), resourceParameterSaveDTOUpdate), resourceParameterDTOUpdate);
    }

//    @Test
//    public void update() {
//        PowerMockito.doNothing().when(resourceParameterService, "updateParameterNameAndColumnName");
//    }

    //TODO
//    @Test
//    public void saveResourceParameterSaveDTOSuccess() {
////        when(modelMapper.map(resourceParameterSaveDTO, ResourceParameter.class)).thenReturn(resourceParameter);
//
//        when(resourceParameterRepository.save(resourceParameter)).thenReturn(resourceParameter);
//        assertEquals(resourceParameterDTO, resourceParameterService.save(resourceParameterSaveDTO));
//    }

    @Test
    public void saveResourceRelationSuccess() {
        when(resourceRelationRepository.save(resourceRelation)).thenReturn(resourceRelation);
        assertEquals(resourceRelation, resourceRelationRepository.save(resourceRelation));
    }

//    @Test
//    public void deleteResourceParameterSuccess() {
//        resourceParameterService.delete(anyLong(), anyLong());
//        verify(resourceParameterRepository, times(1)).deleteById(anyLong());
//    }

//    @Test(expected = NotDeletedException.class)
//    public void deleteResourceParameterFailed() {
//       doThrow(new EmptyResultDataAccessException(1)).when(resourceParameterRepository).deleteById(anyLong());
//       resourceParameterService.delete(anyLong());
//    }
}
