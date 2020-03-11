package com.softserve.rms.search;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.constants.ValidationPattern;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.ResourceRecord;
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
public class SearchAndFilterUtil {
    private DSLContext dslContext;
    private Map<String, Function<String, ?>> map = new HashMap<>();

    /**
     * Constructor with parameters
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public SearchAndFilterUtil(DSLContext dslContext) {
        this.dslContext = dslContext;
        map.put(FieldConstants.LONG.getValue(), Long::valueOf);
        map.put(FieldConstants.INTEGER.getValue(), Integer::valueOf);
        map.put(FieldConstants.DOUBLE.getValue(), Double::valueOf);
        map.put(FieldConstants.BOOLEAN.getValue(), Boolean::valueOf);
        map.put(FieldConstants.STRING.getValue(), s -> s);
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
        List<String> newStrings = splitSearchCriteriaByQuoteAndComma(search);

        List<SearchCriteria> searchStringList = new ArrayList<>();
        Pattern pattern = Pattern.compile(ValidationPattern.SEARCH_PATTERN);
        for (String newString : newStrings) {
            Matcher matcher = pattern.matcher(newString);
            if (matcher.matches()) {
                searchStringList.add(checkTableName(tableName,
                        matcher.group(1), matcher.group(2), matcher.group(3)));
            } else {
                throw new InvalidParametersException(ErrorMessage.INVALID_SEARCH_CRITERIA.getMessage());
            }
        }
        return searchStringList;
    }

    /**
     * Method splits string with search criteria by point.
     *
     * @param search URL string with criteria
     * @return list of {@link String}
     * @author Halyna Yatseniuk
     */
    public List<String> splitSearchCriteriaByQuoteAndComma(String search) {
        if (search != null) {
            return verifyIfCriteriaContainsExtraQuotes(Arrays.asList
                    (search.split(FieldConstants.QUOTE_AND_COMMA.getValue())));
        }
        return null;
    }

    /**
     * Method verifies if string of search criteria value contains an extra quotes.
     *
     * @param searchCriteria list of search criteria valuee {@link String}
     * @return list of trimmed search criteria values {@link String}
     * @author Halyna Yatseniuk
     */
    public List<String> verifyIfCriteriaContainsExtraQuotes(List<String> searchCriteria) {
        for (int i = 0; i < searchCriteria.size(); i++) {
            while (searchCriteria.get(i).contains(FieldConstants.QUOTE.getValue())) {
                searchCriteria.set(i, trimExtraQuotes(searchCriteria.get(i)));
            }
        }
        return searchCriteria;
    }

    /**
     * Method trims extra quotes for search criteria values.
     *
     * @param string search criteria {@link String}
     * @return modified search criteria {@link String}
     * @author Halyna Yatseniuk
     */
    public String trimExtraQuotes(String string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        return stringBuilder.deleteCharAt(stringBuilder.indexOf(FieldConstants.QUOTE.getValue())).toString();
    }

    /**
     * Method checks table name and calls appropriate parameter verification method.
     *
     * @param tableName    name of a table where entities are searched
     * @param matcherGroup array with search criteria divided by groups
     * @return {@link SearchCriteria}
     * @author Halyna Yatseniuk
     */
    public SearchCriteria checkTableName(String tableName, String... matcherGroup) {
        if (tableName.equals(FieldConstants.RESOURCE_TEMPLATES_TABLE.getValue())) {
            return checkColumnParameterTypeForTemplate(tableName,
                    matcherGroup[0], matcherGroup[1], matcherGroup[2]);
        } else {
            return checkColumnParameterTypeForResource(tableName,
                    matcherGroup[0], matcherGroup[1], matcherGroup[2]);
        }
    }

    /**
     * Method applies {@link SearchCriteria} value type due to {@link ResourceRecord} table column type.
     *
     * @param tableName    name of a table where entities are searched
     * @param matcherGroup array with search criteria divided by groups
     * @return {@link SearchCriteria}
     * @author Halyna Yatseniuk
     */
    public SearchCriteria checkColumnParameterTypeForResource(String tableName, String... matcherGroup) {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key(matcherGroup[0])
                .operation(matcherGroup[1]).build();

        List<Field<?>> columns = getColumns(tableName);
        for (Field<?> column : columns) {
            if (column.getName().equals(matcherGroup[0])) {
                String columnType = column.getDataType().getSQLDataType().getType().getSimpleName();
                Function<String, ?> parser = map.get(columnType);
                try {
                    searchCriteria.setValue(parser.apply(matcherGroup[2]));
                } catch (NumberFormatException e) {
                    throw new InvalidParametersException(ErrorMessage.INVALID_SEARCH_CRITERIA.getMessage());
                }
            }
        }
        return searchCriteria;
    }

    /**
     * Method applies {@link SearchCriteria} value type due to {@link ResourceTemplate} table column type.
     *
     * @param tableName    name of a table where entities are searched
     * @param matcherGroup array with search criteria divided by groups
     * @return {@link SearchCriteria}
     * @author Halyna Yatseniuk
     */
    public SearchCriteria checkColumnParameterTypeForTemplate(String tableName, String... matcherGroup) {
        SearchCriteria searchCriteria = SearchCriteria.builder().key(matcherGroup[0]).operation(matcherGroup[1]).build();
        ResourceTemplate resourceTemplate = new ResourceTemplate();

        java.lang.reflect.Field[] fields = resourceTemplate.getClass().getDeclaredFields();

        for (java.lang.reflect.Field field : fields) {
            if (field.getName().equals(matcherGroup[0])) {
                String fieldType = field.getType().getSimpleName();
                Function<String, ?> parser = map.get(fieldType);
                try {
                    searchCriteria.setValue(parser.apply(matcherGroup[2]));
                } catch (NumberFormatException e) {
                    throw new InvalidParametersException(ErrorMessage.INVALID_SEARCH_CRITERIA.getMessage());
                }
            }
        }
        return searchCriteria;
    }

    /**
     * Method selects all columns from a table by provided table name.
     *
     * @param tableName name of a table where entities are searched
     * @return list of {@link Field}
     * @throws InvalidParametersException if the table doesn't exist
     * @author Halyna Yatseniuk
     */

    public List<Field<?>> getColumns(String tableName) {
        Table<?> foundTable;
        try {
            foundTable = dslContext.meta().getTables(tableName).get(0);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidParametersException(ErrorMessage.INVALID_TABLE_CRITERIA.getMessage());
        }
        return Arrays.asList(foundTable.fields());
    }
}