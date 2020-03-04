package com.softserve.rms.util;

import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.repository.implementation.JooqSearching;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ResourceSpecification {
    private DSLContext dslContext;
    private static final Logger LOG = LoggerFactory.getLogger(ResourceSpecification.class);


    @Autowired
    public ResourceSpecification(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public SearchCriteria checkColumnParameterType(String tableName, String... matcherGroup) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey(matcherGroup[0]);
        searchCriteria.setOperation(matcherGroup[1]);

        List<Field<?>> fields = getColumns(tableName);
        for (Field<?> field : fields) {
            if (field.getName().equals(matcherGroup[0])) {
                if (field.getDataType().getSQLDataType().getType().getSimpleName().equals("Double")) {
                    searchCriteria.setValue(Double.parseDouble(matcherGroup[2]));
                } else if (field.getDataType().getSQLDataType().getType().getSimpleName().equals("Long")) {
//                    String string = matcherGroup[2];
                    Long number = Long.parseLong(matcherGroup[2]);
                    searchCriteria.setValue(number);
                } else if (field.getDataType().getSQLDataType().getType().getSimpleName().equals("Integer")) {
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
        List<Field<?>> fields = Arrays.asList(foundTable.fields());
//        LOG.info(fields.get(4).getDataType() + " is get data type");
        LOG.info(fields.get(3).getDataType().getSQLDataType().getType().getSimpleName() + " get simple name");
        LOG.info(fields.get(4).getDataType().getSQLDataType().getType().getSimpleName() + " get simple name");

        return fields;
    }

    public String replaceUnderscoresToSpaces(String searchedText) {
        return searchedText.replace("_", " ");
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
}