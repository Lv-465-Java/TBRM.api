package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.resourcerecord.ResourceRecordDTO;
import com.softserve.rms.dto.resourcerecord.ResourceRecordSaveDTO;
import com.softserve.rms.entities.*;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.ResourceRecordRepository;
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
public class ResourceRecordServiceImplTest {

    @InjectMocks
    private ResourceRecordServiceImpl resourceService;

    @Mock
    private ResourceRecordRepository resourceRecordRepository;

    @Mock
    private ResourceTemplateService resourceTemplateService;

    @Mock
    private UserRepository userRepository;

    private Role role = new Role(3L, "REGISTER");
    private User user = new User(1L, "testName", "testSurname", "testEmail", "any", "any", false, null, Collections.emptyList(), Collections.emptyList());

    private ResourceTemplate resourceTemplate = new ResourceTemplate(1L, "testName", "test_name", null, true, null, Collections.emptyList(), Collections.emptyList());
    private HashMap<String, Object> firstDynamicParameters = new HashMap<String, Object>() {{
        put("first_parameter", 111);
        put("second_parameter", 999);
    }};
    private HashMap<String, Object> secondDynamicParameters = new HashMap<String, Object>() {{
        put("first_parameter", 111111);
        put("second_parameter", 987123);
    }};
    private ResourceRecord resourceRecord = new ResourceRecord(1L, "Test", "Some description", resourceTemplate, user, firstDynamicParameters);
    private ResourceRecord secondResourceRecord = new ResourceRecord(null, "Test", "Some description", resourceTemplate, user, firstDynamicParameters);
    private ResourceRecordDTO resourceRecordDTO = new ResourceRecordDTO(1L, "Test", "Some description", resourceTemplate.getId(), user.getId(), firstDynamicParameters);

    private ResourceRecordSaveDTO resourceRecordSaveDTO = new ResourceRecordSaveDTO("Test", "Some description", resourceTemplate.getId(), user.getId(), firstDynamicParameters);
    private List<ResourceRecord> resourceRecords = Arrays.asList(
            new ResourceRecord(1L, "TestName1", "Some description", resourceTemplate, user, firstDynamicParameters),
            new ResourceRecord(2L, "TestName2", "Some description2", resourceTemplate, user, secondDynamicParameters));
    private List<ResourceRecordDTO> resourceRecordDTOS = Arrays.asList(
            new ResourceRecordDTO(1L, "TestName1", "Some description", resourceTemplate.getId(), user.getId(), firstDynamicParameters),
            new ResourceRecordDTO(2L, "TestName2", "Some description2", resourceTemplate.getId(), user.getId(), secondDynamicParameters));
    private Map<String, Object> map;

    @Test
    public void getListOfResourceDTOsSuccess() {
        when(resourceRecordRepository.findAll(anyString())).thenReturn(resourceRecords);
        assertEquals(resourceRecordDTOS, resourceService.findAll(anyString()));
    }

    @Test
    public void getEmptyListOfResourceDTOs() {
        List<ResourceRecordDTO> expected = Collections.emptyList();
        assertEquals(expected, resourceService.findAll(any()));
    }

    @Test
    public void getResourceByIdSuccess() {
        when(resourceRecordRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resourceRecord));
        assertEquals(resourceRecord, resourceService.findById(resourceTemplate.getTableName(), anyLong()));
    }

    @Test(expected = NotFoundException.class)
    public void getResourceByIdFailed() {
        resourceService.findById(null, null);
    }

    @Test
    public void getResourceParameterByIdDTOSuccess() {
        when(resourceRecordRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resourceRecord));
        assertEquals(resourceRecordDTO, resourceService.findByIdDTO(resourceTemplate.getTableName(), anyLong()));
    }

    @Test(expected = NotFoundException.class)
    public void getResourceParameterDTOByIdFailed() {
        resourceService.findByIdDTO(null, null);
    }

    @Test
    public void deleteSuccess() {
        resourceService.delete(resourceTemplate.getTableName(), resourceRecord.getId());
        verify(resourceRecordRepository, times(1)).delete(resourceTemplate.getTableName(), resourceRecord.getId());
    }

    @Test
    public void saveResource() {
        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
        when(userRepository.getOne(anyLong())).thenReturn(user);
        resourceService.save(resourceTemplate.getTableName(), resourceRecordSaveDTO);
//        SecurityContextHolder.setContext(securityContext);
//        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        verify(resourceRecordRepository, times(1)).save(resourceTemplate.getTableName(), secondResourceRecord);
    }

    @Test
    public void updateResourceWithNullNameAndNullParameters() {
        when(resourceRecordRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resourceRecord));
        map = new HashMap<>();
        map.put("name", null);
        map.put("description", "description");
        map.put("parameters", null);
        resourceService.update(resourceTemplate.getTableName(), resourceRecord.getId(), map);
        verify(resourceRecordRepository, times(1)).update(resourceTemplate.getTableName(), resourceRecord.getId(), resourceRecord);
    }

    @Test
    public void updateResourceWithNullDescriptionAndNullParameters() {
        when(resourceRecordRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resourceRecord));
        map = new HashMap<>();
        map.put("name", "name");
        map.put("description", null);
        map.put("parameters", null);
        resourceService.update(resourceTemplate.getTableName(), resourceRecord.getId(), map);
        verify(resourceRecordRepository, times(1)).update(resourceTemplate.getTableName(), resourceRecord.getId(), resourceRecord);
    }

    @Test
    public void updateResourceWithNullNameAndNullDescription() {
        when(resourceRecordRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resourceRecord));
        map = new HashMap<>();
        map.put("name", null);
        map.put("description", null);
        map.put("parameters", firstDynamicParameters);
        resourceService.update(resourceTemplate.getTableName(), resourceRecord.getId(), map);
        verify(resourceRecordRepository, times(1)).update(resourceTemplate.getTableName(), resourceRecord.getId(), resourceRecord);
    }
}
