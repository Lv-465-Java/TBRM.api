package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.resourceRecord.ResourceRecordDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.*;
import com.softserve.rms.exceptions.InvalidParametersException;
import com.softserve.rms.repository.implementation.FilterRepositoryImpl;
import com.softserve.rms.search.SearchAndFilterUtil;
import com.softserve.rms.search.SpecificationsBuilder;
import org.jooq.Condition;
import org.junit.Assert;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.softserve.rms.constants.FieldConstants.RESOURCE_TEMPLATES_TABLE;
import static com.softserve.rms.util.PaginationUtil.buildPage;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FilterServiceImpl.class)
public class FilterServiceImplTest {
    @InjectMocks
    private FilterServiceImpl filterService;
    @Mock
    private ResourceRecordServiceImpl resourceService;
    @Mock
    private FilterRepositoryImpl filterRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private SearchAndFilterUtil searchAndFilterUtil;

    private String tableName = "room table";
    private Role role = new Role(2L, "MANAGER");
    private User user = new User(1L, "testName", "testSurname", "testEmail", "any",
            "any", false, role, "imageurl", "google", "12444",
            Collections.emptyList(), null, Collections.emptyList());
    private ResourceRecord record = new ResourceRecord(15L, "record", "descr", user, "", "", new HashMap<>());
    private ResourceRecordDTO recordDTO = new ResourceRecordDTO(15L, "record", "descr", 1L, "", "", new HashMap<>());
    private List<ResourceRecord> resourceRecords = new ArrayList<>();
    private List<SearchCriteria> filterCriteriaList = new ArrayList<>();
    private SearchCriteria criteriaName = new SearchCriteria("name", ":", "room");
    private SearchCriteria criteriaDescription = new SearchCriteria("description", "=", "room");
    private SearchCriteria criteriaFloor = new SearchCriteria("floor", "!=", "3");
    private SearchCriteria criteriaSquare = new SearchCriteria("Square", ">", "20.36");
    private SearchCriteria criteriaWorkplaces = new SearchCriteria("Workplaces", "<", "10");

    @Before
    public void initializeMock() {
        filterService = PowerMockito.spy(new FilterServiceImpl(filterRepository, modelMapper,
                searchAndFilterUtil, resourceService));
        filterCriteriaList.add(criteriaName);
        filterCriteriaList.add(criteriaDescription);
        filterCriteriaList.add(criteriaFloor);
        filterCriteriaList.add(criteriaSquare);
        filterCriteriaList.add(criteriaWorkplaces);
        resourceRecords.add(record);
    }

    @Test
    public void testFilterByTwoCriteria() throws Exception {
        when(filterRepository.searchResourceTemplate(anyList(), anyString())).thenReturn(resourceRecords);
        List<ResourceRecordDTO> resourceRecordsList = Whitebox.invokeMethod(
                filterService, "filterByCriteria", filterCriteriaList, "room_table");
        Assert.assertTrue(resourceRecordsList.size() > 0);
    }

    @Test
    public void testConvertCriteriaToConditionMultipleCriteria() throws Exception {
        List<Condition> condition = Whitebox.invokeMethod(filterService, "convertCriteriaToCondition",
                filterCriteriaList);
        Assert.assertFalse(condition.isEmpty());
        assertEquals(condition.size(), filterCriteriaList.size());
    }

    @Test(expected = InvalidParametersException.class)
    public void testConvertCriteriaToConditionFails() throws Exception {
        List<SearchCriteria> searchCriteriaInvalid = new ArrayList<>();
        searchCriteriaInvalid.add(new SearchCriteria("description", "?", "room"));
        Whitebox.invokeMethod(filterService, "convertCriteriaToCondition",
                searchCriteriaInvalid);
    }

    @Test
    public void verifyIfSearchIsEmpty() {
        Page<ResourceRecordDTO> page = new PageImpl<>(Collections.singletonList(recordDTO));
        when(resourceService.findAll(any(String.class), any(Integer.class), any(Integer.class))).thenReturn(page);
        Page<ResourceRecordDTO> templates = filterService.verifyIfFilterIsEmpty("", tableName, 1, 1);
        verify(resourceService, times(1)).findAll(anyString(), anyInt(), anyInt());
        assertEquals(page, templates);
    }

    @Test
    public void verifyIfSearchIsNotEmpty() throws Exception {
        when(searchAndFilterUtil
                .matchSearchCriteriaToPattern(any(String.class), any(String.class))).thenReturn(filterCriteriaList);
        PowerMockito.when(filterService, "filterByCriteria", anyList(), anyString())
                .thenReturn(Collections.singletonList(recordDTO));
        PowerMockito.doReturn(Collections.singletonList(recordDTO))
                .when(filterService, "filterByCriteria", anyList(), anyString());
        Page<ResourceRecordDTO> templates =
                filterService.verifyIfFilterIsEmpty("search", tableName, 1, 1);
        verify(resourceService, times(0)).findAll(anyString(), anyInt(), anyInt());
        verifyPrivate(filterService, times(2)).
                invoke("filterByCriteria", anyList(), anyString());
    }
}