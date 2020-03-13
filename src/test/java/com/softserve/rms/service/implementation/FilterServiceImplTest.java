package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.resourceRecord.ResourceRecordDTO;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.entities.User;
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
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.softserve.rms.constants.FieldConstants.RESOURCE_TEMPLATES_TABLE;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FilterServiceImplTest.class)
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
    @Mock
    private SpecificationsBuilder builder;

    private Role role = new Role(2L, "MANAGER");
    private User user = new User(1L, "testName", "testSurname", "testEmail", "any",
            "any", false, role, "imageurl", "google", "12444",
            Collections.emptyList(), null, Collections.emptyList());
    private ResourceRecord record = new ResourceRecord(15L, "record", "descr", user, "", "", new HashMap<>());
    private List<ResourceRecord> resourceRecords = new ArrayList<>();
    private List<SearchCriteria> searchCriteriaList = new ArrayList<>();
    private SearchCriteria criteriaName = new SearchCriteria("name", ":", "room");
    private SearchCriteria criteriaDescription = new SearchCriteria("description", "=", "room");
    private SearchCriteria criteriaFloor = new SearchCriteria("floor", "!=", "3");
    private SearchCriteria criteriaSquare = new SearchCriteria("Square", ">", "20.36");
    private SearchCriteria criteriaWorkplaces = new SearchCriteria("Workplaces", "<", "10");

    @Before
    public void initializeMock() {
        filterService = PowerMockito.spy(new FilterServiceImpl(filterRepository, modelMapper,
                searchAndFilterUtil, resourceService));
        searchCriteriaList.add(criteriaName);
        searchCriteriaList.add(criteriaDescription);
        searchCriteriaList.add(criteriaFloor);
        searchCriteriaList.add(criteriaSquare);
        searchCriteriaList.add(criteriaWorkplaces);
        resourceRecords.add(record);
    }

    @Test
    public void testFilterByTwoCriteria() throws Exception {
        when(filterRepository.searchResourceTemplate(anyList(), anyString())).thenReturn(resourceRecords);
        List<ResourceRecordDTO> resourceRecordsList = Whitebox.invokeMethod(
                filterService, "filterByCriteria", searchCriteriaList, "room_table");
        Assert.assertTrue(resourceRecordsList.size() > 0);
    }

    @Test
    public void testConvertCriteriaToConditionMultipleCriteria() throws Exception {
        List<Condition> condition = Whitebox.invokeMethod(filterService, "convertCriteriaToCondition",
                searchCriteriaList);
        Assert.assertFalse(condition.isEmpty());
        Assert.assertEquals(condition.size(), searchCriteriaList.size());
    }

    @Test(expected = InvalidParametersException.class)
    public void testConvertCriteriaToConditionFails() throws Exception {
        List<SearchCriteria> searchCriteriaInvalid = new ArrayList<>();
        searchCriteriaInvalid.add(new SearchCriteria("description", "?", "room"));
        Whitebox.invokeMethod(filterService, "convertCriteriaToCondition",
                searchCriteriaInvalid);
    }
}