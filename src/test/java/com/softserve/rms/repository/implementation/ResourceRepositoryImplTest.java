//package com.softserve.rms.repository.implementation;
//
//import com.softserve.rms.entities.Resource;
//import com.softserve.rms.entities.ResourceTemplate;
//import com.softserve.rms.entities.User;
//import com.softserve.rms.repository.implementation.ResourceRepositoryImpl;
//import com.softserve.rms.service.ResourceTemplateService;
//import com.softserve.rms.service.UserService;
//import org.jooq.DSLContext;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public class ResourceRepositoryImplTest {
//
//    @InjectMocks
//    private ResourceRepositoryImpl resourceRepository;
//
//    @Mock
//    private DSLContext dslContext;
//
//    @Mock
//    private ResourceTemplateService resourceTemplateService;
//
//    @Mock
//    private UserService userService;
//
//    private User user = new User(1L, "testName", "testSurname", "testEmail", "any", "any", false, null, Collections.emptyList());
//    private ResourceTemplate resourceTemplate = new ResourceTemplate(1L, "testName", "test_name", null, true, null, Collections.emptyList(), Collections.emptyList());
//    private HashMap<String, Object> firstDynamicParameters = new HashMap<String, Object>() {{
//        put("first_parameter", 111);
//        put("second_parameter", 999);
//    }};
//    private HashMap<String, Object> secondDynamicParameters = new HashMap<String, Object>() {{
//        put("first_parameter", 111111);
//        put("second_parameter", 987123);
//    }};
//
//    private List<Resource> resources = Arrays.asList(
//            new Resource(1L, "TestName1", "Some description", resourceTemplate, user, firstDynamicParameters),
//            new Resource(2L, "TestName2", "Some description2", resourceTemplate, user, secondDynamicParameters));
//
//    @Test
//    public void getListOfResourcesSuccess() {
//        doNothing().when(dslContext.selectFrom(anyString())).fetch();
//        when(resourceRepository.findAll(anyString())).thenReturn(resources);
//        verifyNoMoreInteractions(resourceRepository);
//    }
//}
