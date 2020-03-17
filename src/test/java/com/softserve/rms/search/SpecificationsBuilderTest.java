package com.softserve.rms.search;

import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.SearchCriteria;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.softserve.rms.constants.FieldConstants.RESOURCE_TEMPLATES_TABLE;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SpecificationsBuilder.class)
public class SpecificationsBuilderTest {
    @InjectMocks
    private SpecificationsBuilder specificationsBuilder;
    @Mock
    private SearchAndFilterUtil searchAndFilterUtil;
    @Mock
    private SearchSpecification searchSpecification;

    private List<SearchCriteria> searchCriteriaList = new ArrayList<>();
    private SearchCriteria criteriaName = new SearchCriteria("name", ":", "room");
    private SearchCriteria criteriaDescription = new SearchCriteria("description", "=", "room");

    private List<SearchSpecification> searchSpecifications = new ArrayList<>();
    private SearchSpecification searchSpecificationName = new SearchSpecification
            (criteriaName);
    private SearchSpecification searchSpecificationDesc = new SearchSpecification
            (criteriaDescription);

    @Before
    public void initializeMock() {
        specificationsBuilder = PowerMockito.spy(new SpecificationsBuilder(searchAndFilterUtil));
        searchCriteriaList.add(criteriaName);
        searchCriteriaList.add(criteriaDescription);
        searchSpecifications.add(searchSpecificationName);
        searchSpecifications.add(searchSpecificationDesc);
    }

    @Test
    public void testConvertSearchCriteriaToSpecificationList() {
        List<SearchSpecification> searchSpecificationList = specificationsBuilder.
                convertSearchCriteriaToSpecificationList(searchCriteriaList);
        Assert.assertEquals(searchSpecifications, searchSpecificationList);
    }

    @Test
    public void testBuildSpecification() {
        when(specificationsBuilder.convertSearchCriteriaToSpecificationList(anyList())).thenReturn(searchSpecifications);
        String searchByCriteria = "name:room";
        String tableNameForTesting = RESOURCE_TEMPLATES_TABLE.getValue();
        Specification<ResourceTemplate> result = specificationsBuilder.buildSpecification(searchByCriteria, tableNameForTesting);
        Assert.assertNotNull(result);
    }

    @Test
    public void testBuildSpecificationFails() {
        when(specificationsBuilder.convertSearchCriteriaToSpecificationList(anyList())).thenReturn(Collections.emptyList());
        String searchByCriteria = "name:room";
        String tableNameForTesting = RESOURCE_TEMPLATES_TABLE.getValue();
        Specification<ResourceTemplate> result = specificationsBuilder.buildSpecification(searchByCriteria, tableNameForTesting);
        Assert.assertNull(result);
    }
}