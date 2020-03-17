package com.softserve.rms.search;

import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.exceptions.InvalidParametersException;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SearchAndFilterUtil.class)
public class SearchAndFilterUtilTest {
    @InjectMocks
    private SearchAndFilterUtil searchAndFilterUtil;
    @Mock
    private DSLContext dslContext;
    @Mock
    private Map<String, Function<String, ?>> map;

    private String tableName = "room table";
    private List<String> stringList = new ArrayList<>();
    private List<String> splitStrings = new ArrayList<>();
    private String search = "name:'room',description:'meeting',floor:'2'";
    private String keyName = "name";
    private String operationOne = ":";
    private String value = "room";
    private SearchCriteria searchCriteria = new SearchCriteria("name", ":", "room");

    @Before
    public void initializeMock() {
        searchAndFilterUtil = PowerMockito.spy(new SearchAndFilterUtil(dslContext));
        map.put(FieldConstants.LONG.getValue(), Long::valueOf);
        map.put(FieldConstants.INTEGER.getValue(), Integer::valueOf);
        map.put(FieldConstants.DOUBLE.getValue(), Double::valueOf);
        map.put(FieldConstants.BOOLEAN.getValue(), Boolean::valueOf);
        map.put(FieldConstants.STRING.getValue(), s -> s);
        map.put(FieldConstants.USER_CLASS.getValue(), Long::valueOf);
        stringList.add("name:'room'");
        stringList.add("description:'meeting'");
        stringList.add("floor:'2'");
        splitStrings.add("name:room");
    }

    @Test
    public void splitSearchCriteriaByQuoteAndCommaWithNull() throws Exception {
        List<String> list = Whitebox.invokeMethod(searchAndFilterUtil, "splitSearchCriteriaByQuoteAndComma",
                (Object) null);
        assertNull(list);
    }

    @Test
    public void splitSearchCriteriaByQuoteAndComma() throws Exception {
        String searchCriteria = "name:'room',description:'meeting',floor:'2'";
        List<String> stringList = new ArrayList<>();
        PowerMockito.doReturn(stringList).when(searchAndFilterUtil,
                "verifyIfCriteriaContainsExtraQuotes", anyList());
        List<String> list = Whitebox.invokeMethod(searchAndFilterUtil,
                "splitSearchCriteriaByQuoteAndComma", searchCriteria);
        assertNotNull(list);
        assertEquals(list, stringList);
    }

    @Test
    public void verifyIfCriteriaContainsExtraQuotes() throws Exception {
        List<String> searchList = new ArrayList<>();
        searchList.add("name:room");
        searchList.add("description:meeting");
        searchList.add("floor:2");
        List<String> list = Whitebox.invokeMethod(
                searchAndFilterUtil, "verifyIfCriteriaContainsExtraQuotes", stringList);
        assertEquals(searchList, list);
    }

    @Test
    public void trimFirstExtraQuotes() {
        String string = "name:room'";
        String trimmedString = searchAndFilterUtil.trimExtraQuotes("name:'room'");
        assertEquals(string, trimmedString);
    }

    @Test
    public void trimSecondExtraQuotes() {
        String string = "name:room";
        String trimmedString = searchAndFilterUtil.trimExtraQuotes("name:room'");
        assertEquals(string, trimmedString);
    }

    @Test
    public void matchSearchCriteriaToPattern() throws Exception {
        List<SearchCriteria> expect = new ArrayList<>();
        expect.add(searchCriteria);
        PowerMockito.doReturn(splitStrings)
                .when(searchAndFilterUtil, "splitSearchCriteriaByQuoteAndComma", Mockito.any(String.class));
        PowerMockito.doReturn(searchCriteria)
                .when(searchAndFilterUtil, "checkTableName", Mockito.any(String.class), Mockito.any(String.class));
        List<SearchCriteria> result = searchAndFilterUtil.matchSearchCriteriaToPattern(search, tableName);
        assertEquals(expect, result);
    }

    @Test(expected = InvalidParametersException.class)
    public void matchSearchCriteriaToPatternWithException() throws Exception {
        splitStrings.set(0, "name8room");
        PowerMockito.doReturn(splitStrings)
                .when(searchAndFilterUtil, "splitSearchCriteriaByQuoteAndComma", Mockito.any(String.class));
        searchAndFilterUtil.matchSearchCriteriaToPattern(search, tableName);
    }

    @Test
    public void checkTableNameIsTemplateTable() throws Exception {
        PowerMockito.doReturn(searchCriteria)
                .when(searchAndFilterUtil, "checkColumnParameterTypeForTemplate", Mockito.any(String.class));
        String resourceTemplateTable = "resource_templates";
        SearchCriteria result = Whitebox.invokeMethod(searchAndFilterUtil, "checkTableName",
                resourceTemplateTable, keyName, operationOne, value);
        assertEquals(searchCriteria, result);
        verifyPrivate(searchAndFilterUtil, times(1)).
                invoke("checkColumnParameterTypeForTemplate", Mockito.any(String.class));
        verifyPrivate(searchAndFilterUtil, times(0)).
                invoke("checkColumnParameterTypeForResource", Mockito.any(String.class), Mockito.any());
    }


    @Test
    public void checkTableNameIsResourceTable() throws Exception {
        PowerMockito.doReturn(searchCriteria)
                .when(searchAndFilterUtil, "checkColumnParameterTypeForResource", Mockito.any(String.class),
                        Mockito.any());
        SearchCriteria result = Whitebox.invokeMethod(searchAndFilterUtil, "checkTableName", tableName,
                keyName, operationOne, value);
        assertEquals(searchCriteria, result);
        verifyPrivate(searchAndFilterUtil, times(1)).
                invoke("checkColumnParameterTypeForResource", Mockito.any(String.class), Mockito.any());
        verifyPrivate(searchAndFilterUtil, times(0)).
                invoke("checkColumnParameterTypeForTemplate", Mockito.any(String.class), Mockito.any());
    }

    @Test
    public void checkColumnParameterTypeForTemplate() throws Exception {
        SearchCriteria result = Whitebox.invokeMethod(searchAndFilterUtil, "checkColumnParameterTypeForTemplate",
                keyName, operationOne, value);
        assertEquals(searchCriteria, result);
    }

    @Test(expected = InvalidParametersException.class)
    public void checkColumnParameterTypeForTemplateException() throws Exception {
        String keyId = "id";
        String operationTwo = "=";
        Whitebox.invokeMethod(searchAndFilterUtil, "checkColumnParameterTypeForTemplate",
                keyId, operationTwo, value);
    }
}