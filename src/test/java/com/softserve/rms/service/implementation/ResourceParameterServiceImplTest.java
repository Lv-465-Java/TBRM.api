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
import com.softserve.rms.repository.ResourceParameterRepository;
import com.softserve.rms.repository.ResourceRelationRepository;
import com.softserve.rms.service.ResourceTemplateService;
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
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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
    private ResourceParameter resourceParameter = new ResourceParameter(1L, "resourceParameter", "resource_parameter", ParameterType.POINT_INT, null, null, null);
    private ResourceParameterDTO resourceParameterDTO = new ResourceParameterDTO(1L, "resourceParameter", "resource_parameter", ParameterType.POINT_INT, null, null, null);
//    private ResourceParameterSaveDTO resourceParameterSaveDTO = new ResourceParameterSaveDTO("resource_parameter", ParameterType.POINT_INT, null, 1L, null);
//    private ResourceRelation resourceRelation = new ResourceRelation(1L, resourceParameter, resourceTemplate);
    private ResourceRelationDTO resourceRelationDTO = new ResourceRelationDTO(1L);
    private List<ResourceParameterDTO> parameterDTOS = Arrays.asList(
            new ResourceParameterDTO(1L, "firstParameter", "first_parameter", ParameterType.POINT_INT, null, 1L, null),
            new ResourceParameterDTO(2L, "secondParameter", "second_parameter", ParameterType.POINT_INT, null, 1L, null));

    private List<ResourceParameter> parameters = Arrays.asList(
            new ResourceParameter(1L, "firstParameter", "first_parameter", ParameterType.POINT_INT, null, resourceTemplate, null),
            new ResourceParameter(2L, "secondParameter", "second_parameter", ParameterType.POINT_INT, null, resourceTemplate, null));

//    @Test
//    public void getListOfResourceParameterDTOSuccess() {
//        when(resourceParameterRepository.findAll()).thenReturn(parameters);
//        assertEquals(parameterDTOS, resourceParameterService.findAll());
//    }
//
//    @Test
//    public void getEmptyListOfResourceParameterDTO() {
//        List<ResourceParameterDTO> expected = Collections.emptyList();
//        assertEquals(expected, resourceParameterService.findAll());
//    }
//
//    @Test
//    public void getResourceParametersByTemplateId() {
//        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
//        when(resourceParameterRepository.findAllByResourceTemplateId(anyLong())).thenReturn(parameters);
//        assertEquals(parameterDTOS, resourceParameterService.findAllByTemplateId(anyLong()));
//    }
//
//    @Test
//    public void getResourceParameterByIdSuccess() {
//        when(resourceParameterRepository.findById(anyLong())).thenReturn(Optional.of(resourceParameter));
//        assertEquals(resourceParameter, resourceParameterService.findById(resourceParameter.getId()));
//    }
//
//    @Test(expected = NotFoundException.class)
//    public void getResourceParameterByIdFailed() {
//        resourceParameterService.findById(anyLong());
//    }
//
//    @Test
//    public void getResourceParameterByIdDTOSuccess() {
//        when(resourceParameterRepository.findById(anyLong())).thenReturn(Optional.of(resourceParameter));
//        assertEquals(resourceParameterDTO, resourceParameterService.findByIdDTO(anyLong()));
//    }
//
//    @Test(expected = NotFoundException.class)
//    public void getResourceParameterDTOByIdFailed() {
//        resourceParameterService.findByIdDTO(anyLong());
//    }
//
//    //TODO
//    @Test
//    public void saveResourceParameterSuccess() {
//        when(resourceParameterRepository.save(resourceParameter)).thenReturn(resourceParameter);
//        assertEquals(resourceParameter, resourceParameterRepository.save(resourceParameter));
//    }
//
//    //TODO
////    @Test
////    public void saveResourceParameterSaveDTOSuccess() {
//////        when(modelMapper.map(resourceParameterSaveDTO, ResourceParameter.class)).thenReturn(resourceParameter);
////
////        when(resourceParameterRepository.save(resourceParameter)).thenReturn(resourceParameter);
////        assertEquals(resourceParameterDTO, resourceParameterService.save(resourceParameterSaveDTO));
////    }
//
//    @Test
//    public void saveResourceRelationSuccess() {
//        when(resourceRelationRepository.save(resourceRelation)).thenReturn(resourceRelation);
//        assertEquals(resourceRelation, resourceRelationRepository.save(resourceRelation));
//    }
//
//    @Test
//    public void deleteResourceParameterSuccess() {
//        resourceParameterService.delete(anyLong());
//        verify(resourceParameterRepository, times(1)).deleteById(anyLong());
//    }
//
//    @Test(expected = NotDeletedException.class)
//    public void deleteResourceParameterFailed() {
//       doThrow(new EmptyResultDataAccessException(1)).when(resourceParameterRepository).deleteById(anyLong());
//       resourceParameterService.delete(anyLong());
//    }
}
