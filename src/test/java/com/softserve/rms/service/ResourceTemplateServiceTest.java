package com.softserve.rms.service;

import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.entities.*;
import com.softserve.rms.exceptions.NotDeletedException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateIsPublishedException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateParameterListIsEmpty;
import com.softserve.rms.repository.PersonRepository;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.service.implementation.ResourceTemplateServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ResourceTemplateServiceTest {
    @InjectMocks
    private ResourceTemplateServiceImpl resourceTemplateService;
    @Mock
    private ModelMapper modelMapper = new ModelMapper();
    @Mock
    private ResourceTemplateRepository resourceTemplateRepository;
    @Mock
    private PersonRepository personRepository;

    private Person person = new Person(1L, "testName", "testSurname", "testEmail", "any", "any", "any", Collections.emptyList());
    private ResourceTemplate resourceTemplate = new ResourceTemplate(1L, "name", "name", "description", false, person, Collections.emptyList(), Collections.emptyList());
    private ResourceTemplateSaveDTO resourceTemplateSaveDTO = new ResourceTemplateSaveDTO("name", "description", person.getId());
    private ResourceTemplateDTO resourceTemplateDTO = new ResourceTemplateDTO(null, "name", "name", "description", false, person.getId(), null);
    private ResourceTemplateDTO resourceTempDTO = new ResourceTemplateDTO(1L, "name", "name", "description", false, person.getId(), Collections.emptyList());
    private ResourceTemplateServiceImpl mocks;

    @Before
    public void initializeMock() {
        mocks = PowerMockito.spy(new ResourceTemplateServiceImpl(resourceTemplateRepository, personRepository));
    }

    @Test
    public void testSaveResourceTemplate() {
        when(personRepository.getOne(anyLong())).thenReturn(person);
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
    public void testFindAllByUserId() {
        when(resourceTemplateRepository.findAllByPersonId(anyLong())).thenReturn(Collections.singletonList(resourceTemplate));
        List<ResourceTemplateDTO> resourceTemplateDTOs = Collections.singletonList(resourceTempDTO);
        assertEquals(resourceTemplateDTOs, resourceTemplateService.getAllByPersonId(anyLong()));
    }

    @Test
    public void testUpdateResourceTemplate() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        when(resourceTemplateRepository.save(any())).thenReturn(resourceTemplate);
        resourceTemplateService.updateById(anyLong(), resourceTemplateSaveDTO);
        ResourceTemplate resourceTemp = resourceTemplateService.findEntityById(anyLong());
        assertEquals(resourceTemplate, resourceTemp);
    }

    @Test
    public void testDeleteById() {
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
        Map<String, String> myAnyMap = new HashMap<>();
        myAnyMap.put("search", anyString());
        assertEquals(resourceTemplateDTOs, resourceTemplateService.searchByNameOrDescriptionContaining(myAnyMap));
    }

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

    @Test(expected = ResourceTemplateParameterListIsEmpty.class)
    public void testVerificationOfResourceTemplateHavingParametersFail() throws Exception {
        resourceTemplate.setResourceParameters(Collections.emptyList());
        Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateHasParameters", resourceTemplate);
    }

    @Test
    public void testVerificationOfResourceTemplateHavingParameters() throws Exception {
        resourceTemplate.setResourceParameters(Collections.singletonList(new ResourceParameter(null, "name",
                "name", ParameterType.AREA_DOUBLE, null, resourceTemplate, null)));
        Boolean result = Whitebox.invokeMethod(mocks, "verifyIfResourceTemplateHasParameters",
                resourceTemplate);
        assertTrue(result);
    }


    @Test
    public void testPublishOfResourceTemplatePassed() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        resourceTemplate.setResourceParameters(Collections.singletonList(new ResourceParameter(null, "name",
                "name", ParameterType.AREA_DOUBLE, null, resourceTemplate, null)));
        Boolean result = resourceTemplateService.publishResourceTemplate(resourceTemplate.getId());
        assertTrue(result);
    }

    @Test(expected = ResourceTemplateIsPublishedException.class)
    public void testPublishOfResourceTemplatePublishedExceptionFailed() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        resourceTemplate.setIsPublished(true);
        resourceTemplate.setResourceParameters(Collections.singletonList(new ResourceParameter(null, "name",
                "name", ParameterType.AREA_DOUBLE, null, resourceTemplate, null)));
        Boolean result = resourceTemplateService.publishResourceTemplate(resourceTemplate.getId());
    }

    @Test(expected = ResourceTemplateParameterListIsEmpty.class)
    public void testPublishOfResourceTemplateParameterListIsEmptyFail() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        resourceTemplate.setResourceParameters(Collections.emptyList());
        Boolean result = resourceTemplateService.publishResourceTemplate(resourceTemplate.getId());
    }

    @Test(expected = ResourceTemplateIsPublishedException.class)
    public void testPublishOfResourceTemplateException() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        resourceTemplate.setIsPublished(true);
        resourceTemplate.setResourceParameters(Collections.emptyList());
        Boolean result = resourceTemplateService.publishResourceTemplate(resourceTemplate.getId());
    }

    @Test
    public void testUnPublishOfResourceTemplateWithValueTrue() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        resourceTemplate.setIsPublished(true);
        assertTrue(resourceTemplateService.unPublishResourceTemplate(resourceTemplate.getId()));
    }

    @Test
    public void testUnPublishOfResourceTemplateWithValueFalse() {
        when(resourceTemplateRepository.findById(anyLong())).thenReturn(Optional.of(resourceTemplate));
        assertTrue(resourceTemplateService.unPublishResourceTemplate(resourceTemplate.getId()));
    }
}