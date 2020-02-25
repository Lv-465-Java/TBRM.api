package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.resourcerecord.ResourceRecordDTO;
import com.softserve.rms.dto.resourcerecord.ResourceRecordSaveDTO;
import com.softserve.rms.entities.*;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateIsNotPublishedException;
import com.softserve.rms.repository.ResourceRecordRepository;
import com.softserve.rms.repository.ResourceRelationRepository;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.ResourceTemplateService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ResourceRecordServiceImpl.class)
public class ResourceRecordServiceImplTest {

    @InjectMocks
    private ResourceRecordServiceImpl resourceRecordService;

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
    private ResourceRecordDTO resourceRecordDTO = new ResourceRecordDTO(1L, "Test", "Some description", resourceTemplate.getId(), user.getId(), secondDynamicParameters);

    private ResourceRecordSaveDTO resourceRecordSaveDTO = new ResourceRecordSaveDTO("Test", "Some description", user.getId(), firstDynamicParameters);
    private ResourceRecordSaveDTO resourceRecordUpdateDTO = new ResourceRecordSaveDTO("TestUpdate", "Some description update", user.getId(), firstDynamicParameters);
    private List<ResourceRecord> resourceRecords = Arrays.asList(
            new ResourceRecord(1L, "TestName1", "Some description", resourceTemplate, user, firstDynamicParameters),
            new ResourceRecord(2L, "TestName2", "Some description2", resourceTemplate, user, secondDynamicParameters));
    private List<ResourceRecordDTO> resourceRecordDTOS = Arrays.asList(
            new ResourceRecordDTO(1L, "TestName1", "Some description", resourceTemplate.getId(), user.getId(), firstDynamicParameters),
            new ResourceRecordDTO(2L, "TestName2", "Some description2", resourceTemplate.getId(), user.getId(), secondDynamicParameters));
    private Map<String, Object> map;

    @Before
    public void initializeMock() {
        resourceRecordService = PowerMockito.spy(new ResourceRecordServiceImpl(resourceRecordRepository,
                resourceTemplateService, userRepository));
    }

    @Test
    public void getListOfResourceDTOsSuccess() throws Exception {
        PowerMockito.doReturn(resourceTemplate).when(resourceRecordService, "checkIfResourceTemplateIsPublished", anyString());
        when(resourceRecordRepository.findAll(anyString())).thenReturn(resourceRecords);
        assertEquals(resourceRecordDTOS, resourceRecordService.findAll(anyString()));
    }

    @Test
    public void getEmptyListOfResourceDTOs() throws Exception {
        PowerMockito.doReturn(resourceTemplate).when(resourceRecordService, "checkIfResourceTemplateIsPublished", anyString());
        List<ResourceRecordDTO> expected = Collections.emptyList();
        assertEquals(expected, resourceRecordService.findAll(any()));
    }

    @Test
    public void getResourceByIdSuccess() throws Exception {
        PowerMockito.doReturn(resourceTemplate).when(resourceRecordService, "checkIfResourceTemplateIsPublished", anyString());
//        Whitebox.invokeMethod(resourceRecordService, "checkIfResourceTemplateIsPublished", anyString());
        when(resourceRecordRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resourceRecord));
        assertEquals(resourceRecord, resourceRecordService.findById(resourceTemplate.getTableName(), anyLong()));
    }

    @Test(expected = NotFoundException.class)
    public void getResourceByIdFailed() throws Exception {
        PowerMockito.doReturn(resourceTemplate).when(resourceRecordService, "checkIfResourceTemplateIsPublished", anyString());
        resourceRecordService.findById(null, null);
    }

    @Test
    public void getResourceParameterByIdDTOSuccess() {
        when(resourceRecordRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resourceRecord));
        assertEquals(resourceRecordDTO, resourceRecordService.findByIdDTO(resourceTemplate.getTableName(), anyLong()));
    }

    @Test(expected = NotFoundException.class)
    public void getResourceParameterDTOByIdFailed() {
        resourceRecordService.findByIdDTO(null, null);
    }

    @Test
    public void deleteSuccess() throws Exception {
        PowerMockito.doReturn(resourceTemplate).when(resourceRecordService, "checkIfResourceTemplateIsPublished", anyString());
        resourceRecordService.delete(resourceTemplate.getTableName(), resourceRecord.getId());
        verify(resourceRecordRepository, times(1)).delete(resourceTemplate.getTableName(), resourceRecord.getId());
    }

    @Test
    public void saveResource() throws Exception {
//        Whitebox.invokeMethod(resourceRecordService, "checkIfResourceTemplateIsPublished", anyString());
        PowerMockito.doReturn(resourceTemplate).when(resourceRecordService, "checkIfResourceTemplateIsPublished", anyString());
//        when(resourceTemplateService.findEntityById(anyLong())).thenReturn(resourceTemplate);
        when(userRepository.getOne(anyLong())).thenReturn(user);
        resourceRecordService.save(resourceTemplate.getTableName(), resourceRecordSaveDTO);
//        SecurityContextHolder.setContext(securityContext);
//        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        verify(resourceRecordRepository, times(1)).save(resourceTemplate.getTableName(), secondResourceRecord);
    }

    @Test
    public void updateResourceRecordSuccess() throws Exception {
        PowerMockito.doReturn(resourceTemplate).when(resourceRecordService, "checkIfResourceTemplateIsPublished", anyString());
        when(resourceRecordRepository.findById(anyString(), anyLong())).thenReturn(Optional.of(resourceRecord));
        resourceRecordService.update(resourceTemplate.getTableName(), resourceRecord.getId(), resourceRecordUpdateDTO);
        verify(resourceRecordRepository, times(1)).update(resourceTemplate.getTableName(), resourceRecord.getId(), resourceRecord);
    }

    @Test
    public void checkIfResourceTemplateIsPublishedSuccess() throws Exception {
        when(resourceTemplateService.findByName(anyString())).thenReturn(resourceTemplate);
        ResourceTemplate actual = Whitebox.invokeMethod(resourceRecordService, "checkIfResourceTemplateIsPublished", anyString());
        assertEquals(resourceTemplate, actual);
    }

    @Test(expected = ResourceTemplateIsNotPublishedException.class)
    public void checkIfResourceTemplateIsPublishedFailed() throws Exception {
        resourceTemplate.setIsPublished(false);
        when(resourceTemplateService.findByName(anyString())).thenReturn(resourceTemplate);
        Whitebox.invokeMethod(resourceRecordService, "checkIfResourceTemplateIsPublished", anyString());
    }

//    @Test(expected = ResourceTemplateIsNotPublishedException.class)
//    public void updateResourceRecordFailed() throws Exception {
//        PowerMockito.doThrow(new ResourceTemplateIsNotPublishedException(ErrorMessage.RESOURCE_TEMPLATE_IS_NOT_PUBLISHED.getMessage()))
//                .when(resourceRecordService, "checkIfResourceTemplateIsPublished", anyString());
//        resourceRecordService.update(resourceTemplate.getTableName(), resourceRecord.getId(), resourceRecordUpdateDTO);
//    }
}
