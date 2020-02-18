package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.resource.ResourceDTO;
import com.softserve.rms.dto.resource.ResourceSaveDTO;
import com.softserve.rms.entities.*;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.ResourceRepository;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.ResourceTemplateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ResourceServiceImplTest {

    @InjectMocks
    private ResourceServiceImpl resourceService;

    @Mock
    private ResourceRepository resourceRepository;

    @Mock
    private ResourceTemplateService resourceTemplateService;

    @Mock
    private UserRepository userRepository;

    private Role role = new Role(3L, "REGISTER");
    private User user = new User(1L, "testName", "testSurname", "testEmail", "any", "any", false, null, Collections.emptyList());

    private ResourceTemplate resourceTemplate = new ResourceTemplate(1L, "testName", "test_name", null, true, null, Collections.emptyList(), Collections.emptyList());
    private HashMap<String, Object> firstDynamicParameters = new HashMap<String, Object>() {{
        put("first_parameter", 111);
        put("second_parameter", 999);
    }};
    private HashMap<String, Object> secondDynamicParameters = new HashMap<String, Object>() {{
        put("first_parameter", 111111);
        put("second_parameter", 987123);
    }};
    private Resource resource = new Resource(1L, "Test", "Some description", resourceTemplate, user, firstDynamicParameters);
    private Resource secondResource = new Resource(null, "Test", "Some description", resourceTemplate, user, firstDynamicParameters);
    private ResourceDTO resourceDTO = new ResourceDTO(1L, "Test", "Some description", resourceTemplate.getId(), user.getId(), firstDynamicParameters);

    private ResourceSaveDTO resourceSaveDTO = new ResourceSaveDTO("Test", "Some description", resourceTemplate.getId(), user.getId(), firstDynamicParameters);
    private List<Resource> resources = Arrays.asList(
            new Resource(1L, "TestName1", "Some description", resourceTemplate, user, firstDynamicParameters),
            new Resource(2L, "TestName2", "Some description2", resourceTemplate, user, secondDynamicParameters));
    private List<ResourceDTO> resourceDTOS = Arrays.asList(
            new ResourceDTO(1L, "TestName1", "Some description", resourceTemplate.getId(), user.getId(), firstDynamicParameters),
            new ResourceDTO(2L, "TestName2", "Some description2", resourceTemplate.getId(), user.getId(), secondDynamicParameters));
    private Map<String, Object> map;

    @Test
    public void getListOfResourceDTOsSuccess() {
        when(resourceRepository.findAll(anyString())).thenReturn(resources);
        assertEquals(resourceDTOS, resourceService.findAll(anyString()));
    }

    @Test
    public void getEmptyListOfResourceDTOs() {
        List<ResourceDTO> expected = Collections.emptyList();
        assertEquals(expected, resourceService.findAll(any()));
    }

    @Test
    public void getResourceByIdSuccess() {
        when(resourceRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resource));
        assertEquals(resource, resourceService.findById(resourceTemplate.getTableName(), anyLong()));
    }

    @Test(expected = NotFoundException.class)
    public void getResourceByIdFailed() {
        resourceService.findById(null, null);
    }

    @Test
    public void getResourceParameterByIdDTOSuccess() {
        when(resourceRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resource));
        assertEquals(resourceDTO, resourceService.findByIdDTO(resourceTemplate.getTableName(), anyLong()));
    }

    @Test(expected = NotFoundException.class)
    public void getResourceParameterDTOByIdFailed() {
        resourceService.findByIdDTO(null, null);
    }

    @Test
    public void deleteSuccess() {
        resourceService.delete(resourceTemplate.getTableName(), resource.getId());
        verify(resourceRepository, times(1)).delete(resourceTemplate.getTableName(), resource.getId());
    }

    @Test
    public void saveResource() {
        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
        when(userRepository.getOne(anyLong())).thenReturn(user);
        resourceService.save(resourceTemplate.getTableName(), resourceSaveDTO);
//        SecurityContextHolder.setContext(securityContext);
//        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        verify(resourceRepository, times(1)).save(resourceTemplate.getTableName(), secondResource);
    }

    @Test
    public void updateResourceWithNullNameAndNullParameters() {
        when(resourceRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resource));
        map = new HashMap<>();
        map.put("name", null);
        map.put("description", "description");
        map.put("parameters", null);
        resourceService.update(resourceTemplate.getTableName(), resource.getId(), map);
        verify(resourceRepository, times(1)).update(resourceTemplate.getTableName(), resource.getId(), resource);
    }

    @Test
    public void updateResourceWithNullDescriptionAndNullParameters() {
        when(resourceRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resource));
        map = new HashMap<>();
        map.put("name", "name");
        map.put("description", null);
        map.put("parameters", null);
        resourceService.update(resourceTemplate.getTableName(), resource.getId(), map);
        verify(resourceRepository, times(1)).update(resourceTemplate.getTableName(), resource.getId(), resource);
    }

    @Test
    public void updateResourceWithNullNameAndNullDescription() {
        when(resourceRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resource));
        map = new HashMap<>();
        map.put("name", null);
        map.put("description", null);
        map.put("parameters", firstDynamicParameters);
        resourceService.update(resourceTemplate.getTableName(), resource.getId(), map);
        verify(resourceRepository, times(1)).update(resourceTemplate.getTableName(), resource.getId(), resource);
    }
}
