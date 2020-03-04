package com.softserve.rms.util;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.constants.ValidationPattern;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.exceptions.BadRequestException;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ResourceFilterUtil {
    private DSLContext dslContext;
    private static final Logger LOG = LoggerFactory.getLogger(ResourceFilterUtil.class);

    @Autowired
    public ResourceFilterUtil(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<String> splitSearchCriteriaByPoint(String search) {
        if (search != null) {
            return Arrays.asList(search.split(FieldConstants.COMMA.getValue()));
        }
        return null;
    }

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

    public SearchCriteria checkColumnParameterType(String tableName, String... matcherGroup) {
        SearchCriteria searchCriteria = SearchCriteria.builder()
                .key(matcherGroup[0])
                .operation(matcherGroup[1]).build();

        List<Field<?>> fields = getColumns(tableName);
        for (Field<?> field : fields) {
            if (field.getName().equals(matcherGroup[0])) {
                if (field.getDataType().getSQLDataType().getType().getSimpleName()
                        .equals(FieldConstants.DOUBLE.getValue())) {
                    searchCriteria.setValue(Double.parseDouble(matcherGroup[2]));
                } else if (field.getDataType().getSQLDataType().getType().getSimpleName()
                        .equals(FieldConstants.LONG.getValue())) {
                    Long number = Long.parseLong(matcherGroup[2]);
                    searchCriteria.setValue(number);
                } else if (field.getDataType().getSQLDataType().getType().getSimpleName()
                        .equals(FieldConstants.INTEGER.getValue())) {
                    searchCriteria.setValue(Integer.parseInt(matcherGroup[2]));
                } else {
                    searchCriteria.setValue(replaceUnderscoresToSpaces(matcherGroup[2]));
                }
            }
        }
        return searchCriteria;
    }

    public List<Field<?>> getColumns(String tableName) {
        Table<?> foundTable = dslContext.meta().getTables(tableName).get(0);
        return Arrays.asList(foundTable.fields());
    }

    public String replaceUnderscoresToSpaces(String searchedText) {
        return searchedText.replace(FieldConstants.UNDERSCORE.getValue(), FieldConstants.SPACE.getValue());
    }
}


//            if (parameter.getColumnName().equals(matcherGroup[0])) {
//                String type = parameter.getParameterType().getType();
//                if (type.equals("Integer")) {
//                    Integer number = Integer.parseInt(matcherGroup[3]);
//                    searchCriteria.setValue(number);
//                }
//                if (type.equals("Double")) {
//                    Double number = Double.parseDouble(matcherGroup[3]);
//                    searchCriteria.setValue(number);
//                }
//                //needed for testing
//                if (type.equals("String")) {
//                    searchCriteria.setValue(matcherGroup[3]);
//                }
//                //needed for testing
//                else {
//                    Boolean bool = Boolean.getBoolean(matcherGroup[3]);
//                    searchCriteria.setValue(bool);
//                }
//            }