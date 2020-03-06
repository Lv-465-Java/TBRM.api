package com.softserve.rms.util;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.constants.ValidationPattern;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.exceptions.BadRequestException;
import com.softserve.rms.exceptions.InvalidParametersException;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ResourceFilterUtil {
    private DSLContext dslContext;
    private Map<String, Function> map = new HashMap<>();

    @Autowired
    public ResourceFilterUtil(DSLContext dslContext) {
        this.dslContext = dslContext;
        map.put("Long", l -> Long.valueOf((String) l));
        map.put("Integer", i -> Integer.valueOf((String) i));
        map.put("String", s -> replaceUnderscoresToSpaces((String) s));
    }

    /**
     * Method matches search criteria to pattern.
     *
     * @param search    URL string with criteria
     * @param tableName name of a table where entities are searched
     * @return list of {@link SearchCriteria}
     * @throws BadRequestException if the search criteria length is not equal with length of matcher groups
     * @author Halyna Yatseniuk
     */
    public List<SearchCriteria> matchSearchCriteriaToPattern(String search, String tableName) {
        List<String> searchStringList = splitSearchCriteriaByPoint(search);
        List<SearchCriteria> criteria = new ArrayList<>();

        for (String string : searchStringList) {
            int searchCriteriaLength = string.length();
            Pattern pattern = Pattern.compile(ValidationPattern.SEARCH_PATTERN);
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                int matcherLength = matcher.group(1).length() + matcher.group(2).length() + matcher.group(3).length();
                if (searchCriteriaLength == matcherLength) {
                    SearchCriteria searchCriteria = checkColumnParameterType(tableName,
                            matcher.group(1), matcher.group(2), matcher.group(3));
                    criteria.add(searchCriteria);
                } else {
                    throw new BadRequestException(ErrorMessage.WRONG_SEARCH_CRITERIA.getMessage());
                }
            }
        }
        return criteria;
    }

    /**
     * Method splits string with search criteria by point.
     *
     * @param search URL string with criteria
     * @return list of {@link String}
     * @author Halyna Yatseniuk
     */

    public List<String> splitSearchCriteriaByPoint(String search) {
        if (search != null) {
            return Arrays.asList(search.split(FieldConstants.COMMA.getValue()));
        }
        return null;
    }

    /**
     * Method applies {@link SearchCriteria} value type due to table column type.
     *
     * @param tableName    name of a table where entities are searched
     * @param matcherGroup array with search criteria divided by groups
     * @return {@link SearchCriteria}
     * @author Halyna Yatseniuk
     */
    public SearchCriteria checkColumnParameterType(String tableName, String... matcherGroup) {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key(matcherGroup[0])
                .operation(matcherGroup[1]).build();

        List<Field<?>> fields = getColumns(tableName);
        for (Field<?> field : fields) {
            if (field.getName().equals(matcherGroup[0])) {
                String columnType = field.getDataType().getSQLDataType().getType().getSimpleName();
                Function parser = map.get(columnType);
                try {
                    searchCriteria.setValue(parser.apply(matcherGroup[2]));
                } catch (NumberFormatException e) {
                    throw new InvalidParametersException("error");
                }
            }
        }
        return searchCriteria;
    }
//    public SearchCriteria checkColumnParameterType(String tableName, String... matcherGroup) {
//        SearchCriteria searchCriteria = SearchCriteria.builder()
//                .key(matcherGroup[0])
//                .operation(matcherGroup[1]).build();
//
//        List<Field<?>> fields = getColumns(tableName);
//        for (Field<?> field : fields) {
//            if (field.getName().equals(matcherGroup[0])) {
//                if (field.getDataType().getSQLDataType().getType().getSimpleName()
//                        .equals(FieldConstants.DOUBLE.getValue())) {
//                    searchCriteria.setValue(Double.parseDouble(matcherGroup[2]));
//                } else if (field.getDataType().getSQLDataType().getType().getSimpleName()
//                        .equals(FieldConstants.LONG.getValue())) {
//                    Long number = Long.parseLong(matcherGroup[2]);
//                    searchCriteria.setValue(number);
//                } else if (field.getDataType().getSQLDataType().getType().getSimpleName()
//                        .equals(FieldConstants.INTEGER.getValue())) {
//                    searchCriteria.setValue(Integer.parseInt(matcherGroup[2]));
//                } else {
//                    searchCriteria.setValue(replaceUnderscoresToSpaces(matcherGroup[2]));
//                }
//            }
//        }
//        return searchCriteria;
//    }

    /**
     * Method selects all columns from a table by provided table name.
     *
     * @param tableName name of a table where entities are searched
     * @return list of {@link Field}
     * @author Halyna Yatseniuk
     */

    public List<Field<?>> getColumns(String tableName) {
        Table<?> foundTable = dslContext.meta().getTables(tableName).get(0);
        return Arrays.asList(foundTable.fields());
    }

    /**
     * Method replaces underscores to spaces for a provided string.
     *
     * @param searchedText string to be modified
     * @return modified string
     * @author Halyna Yatseniuk
     */
    public String replaceUnderscoresToSpaces(String searchedText) {
        return searchedText.replace(FieldConstants.UNDERSCORE.getValue(), FieldConstants.SPACE.getValue());
    }
}