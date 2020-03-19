package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.InvalidParametersException;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.search.SearchAndFilterUtil;
import com.softserve.rms.search.SpecificationsBuilder;
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
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.softserve.rms.constants.FieldConstants.RESOURCE_TEMPLATES_TABLE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SearchServiceImpl.class)
public class SearchServiceImplTest {
    @InjectMocks
    private SearchServiceImpl searchService;
    @Mock
    private ResourceTemplateRepository templateRepository;
    @Mock
    private ResourceTemplateServiceImpl templateService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private SearchAndFilterUtil searchAndFilterUtil;
    @Mock
    private SpecificationsBuilder builder;
    @Mock
    private Specification<ResourceTemplate> specification;

    private String tableNameForTesting = RESOURCE_TEMPLATES_TABLE.getValue();
    private Role role = new Role(2L, "MANAGER");
    private User user = new User(1L, "testName", "testSurname", "testEmail", "any",
            "any", false, role, "imageurl", "google", "12444",
            Collections.emptyList(), null, Collections.emptyList());
    private ResourceTemplateDTO resourceTemplateDTO = new ResourceTemplateDTO(5L, "testNameForSearchDTO",
            "dbTableName", "test test desc", true, user.getFirstName(), user.getLastName(), 2L, Collections.emptyList());
    private ResourceTemplate resourceTemplate = new ResourceTemplate(1L, "testNameForSearch", "dbTableName",
            "test test desc", true, user, Collections.emptyList(), Collections.emptyList());
    private List<ResourceTemplate> testResourceTemplates = new ArrayList<>();

    @Before
    public void initializeMock() {
        searchService = PowerMockito.spy(new SearchServiceImpl(modelMapper,
                templateRepository, templateService, searchAndFilterUtil));
        testResourceTemplates.add(resourceTemplate);
    }

    @Test
    public void verifyIfSearchIsEmpty() throws Exception {
        Page<ResourceTemplateDTO> page = new PageImpl<>(Collections.singletonList(resourceTemplateDTO));
        when(templateService.getAll(any(Integer.class), any(Integer.class))).thenReturn(page);
        Page<ResourceTemplateDTO> templates = searchService.verifyIfSearchIsEmpty("", 1, 1);
        verify(templateService, times(1)).getAll(anyInt(), anyInt());
        verifyPrivate(searchService, times(0)).
                invoke("searchBySpecification", Mockito.any(String.class), Mockito.any(String.class));
    }

    @Test
    public void verifyIfSearchIsNotEmpty() throws Exception {
        Page<ResourceTemplateDTO> page = new PageImpl<>(Collections.singletonList(resourceTemplateDTO));
        when(templateService.getAll(any(Integer.class), any(Integer.class))).thenReturn(page);
        Page<ResourceTemplateDTO> templates = searchService.verifyIfSearchIsEmpty("search", 1, 1);
        verifyPrivate(searchService, times(1)).
                invoke("searchBySpecification", Mockito.any(String.class), Mockito.any(String.class));
        verify(templateService, times(0)).getAll(anyInt(), anyInt());
    }

    @Test
    public void testSearchByNameCriteria() throws Exception {
        when(builder.buildSpecification(any(String.class), any(String.class))).thenReturn(specification);
        when(templateRepository.findAll((Specification<ResourceTemplate>) any())).thenReturn(testResourceTemplates);
        String searchByNameCriteria = "name:room";
        List<ResourceTemplateDTO> resourceTemplates = Whitebox.invokeMethod(
                searchService, "searchBySpecification", searchByNameCriteria, tableNameForTesting);
        Assert.assertTrue(resourceTemplates.size() > 0);
    }

    @Test(expected = InvalidParametersException.class)
    public void testSearchByInvalidCriteriaFails() throws Exception {
        when(builder.buildSpecification(any(String.class), any(String.class))).thenReturn(specification);
        when(templateRepository.findAll((Specification<ResourceTemplate>) any()))
                .thenThrow(new InvalidDataAccessApiUsageException("Your search criteria are not valid"));
        String searchByInvalidCriteria = "name?room";
        Whitebox.invokeMethod(searchService, "searchBySpecification",
                searchByInvalidCriteria, tableNameForTesting);
    }
}