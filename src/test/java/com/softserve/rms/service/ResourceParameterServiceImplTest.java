package com.softserve.rms.service;

import com.softserve.rms.dto.resourceparameter.ResourceParameterDTO;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.ResourceParameterRepository;
import com.softserve.rms.repository.ResourceRelationRepository;
import com.softserve.rms.service.implementation.ResourceParameterServiceImpl;
import com.softserve.rms.util.RangeIntegerPatternGenerator;
import com.softserve.rms.util.Validator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
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

    private ResourceTemplate resourceTemplate = new ResourceTemplate(1L, "template", "resource_template", "some description", false, null, null, null);
    private ResourceParameter resourceParameter = new ResourceParameter(1L, "resourceParameter", "resource_parameter", null, null, null, null);
    private ResourceParameterDTO resourceParameterDTO = new ResourceParameterDTO(1L, "resourceParameter", "resource_parameter", null, null, null, null);

    private List<ResourceParameterDTO> parameterDTOS = Arrays.asList(
            new ResourceParameterDTO(1L, "firstParameter", "first_parameter", null, null, 1L, null),
            new ResourceParameterDTO(2L, "secondParameter", "second_parameter", null, null, 1L, null));

    private List<ResourceParameter> parameters = Arrays.asList(
            new ResourceParameter(1L, "firstParameter", "first_parameter", null, null, resourceTemplate, null),
            new ResourceParameter(2L, "secondParameter", "second_parameter", null, null, resourceTemplate, null));


    @Test
    public void getListOfResourceParameterDTOSuccess() {
        when(resourceParameterRepository.findAll()).thenReturn(parameters);
        when(modelMapper.map(parameters, new TypeToken<List<ResourceParameterDTO>>() {
        }.getType())).thenReturn(parameterDTOS);
        assertEquals(parameterDTOS, resourceParameterService.findAll());
    }

    @Test
    public void getEmptyListOfResourceParameterDTO() {
        List<ResourceParameterDTO> expected = Collections.emptyList();
        when(modelMapper.map(resourceParameterRepository.findAll(), new TypeToken<List<ResourceParameterDTO>>() {
        }.getType())).thenReturn(expected);
        assertEquals(expected, resourceParameterService.findAll());
    }

    @Test
    public void getResourceParametersByTemplateId() {
        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
        when(resourceParameterRepository.findAllByResourceTemplateId(anyLong())).thenReturn(parameters);
        when(modelMapper.map(parameters, new TypeToken<List<ResourceParameterDTO>>() {
        }.getType())).thenReturn(parameterDTOS);
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
    public void getResourceParameterDTOSuccess() {
        when(resourceParameterRepository.findById(anyLong())).thenReturn(Optional.of(resourceParameter));
        when(modelMapper.map(resourceParameter, ResourceParameterDTO.class)).thenReturn(resourceParameterDTO);
        assertEquals(resourceParameterDTO, resourceParameterService.findByIdDTO(anyLong()));
    }

    @Test(expected = NotFoundException.class)
    public void getResourceParameterDTOFailed() {
        resourceParameterService.findByIdDTO(anyLong());
    }




}
