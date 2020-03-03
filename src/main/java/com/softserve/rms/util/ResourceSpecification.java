package com.softserve.rms.util;

import com.softserve.rms.controller.ResourceTemplateController;
import com.softserve.rms.entities.*;
import com.softserve.rms.service.implementation.ResourceTemplateServiceImpl;
import org.joda.time.chrono.AssembledChronology;
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
    //    private final List<SearchCriteria> searchCriteriaList;
//    private SearchCriteria searchCriteria;
    private static final Logger LOG = LoggerFactory.getLogger(ResourceSpecification.class);
    private ResourceTemplateServiceImpl resourceTemplateService;
    private DSLContext dslContext;

    @Autowired
    public ResourceSpecification(ResourceTemplateServiceImpl resourceTemplateService, DSLContext dslContext) {
        this.resourceTemplateService = resourceTemplateService;
        this.dslContext = dslContext;
    }

    //    @Autowired
//    public ResourceSpecification(SearchCriteria searchCriteria) {
//        this.searchCriteria = searchCriteria;
////        this.searchCriteriaList = new ArrayList<>();
//    }

//    @Override
//    public Predicate toPredicate(final Root<ResourceRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
//        switch (searchCriteria.getOperation()) {
//            case EQUAL:
//                return builder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
//            case NOT_EQUAL:
//                return builder.notEqual(root.get(searchCriteria.getKey()), searchCriteria.getValue());
//            case GREATER_THAN:
//                return builder.greaterThan(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
//            case LESS_THAN:
//                return builder.lessThan(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
//            case CONTAINS:
//                return builder.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
//            default:
//                return null;
//        }
//    }

    public SearchCriteria checkColumnParameterType(String tableName, String... matcherGroup) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setKey(matcherGroup[0]);
        searchCriteria.setOperation(matcherGroup[1]);

        List<Field<?>> fields = getColumns(tableName);
        for (Field<?> field : fields) {
            if (field.getName().equals(matcherGroup[0])) {
                if (field.getDataType().hasPrecision()) {
                    Double number = Double.parseDouble(matcherGroup[3]);
                    searchCriteria.setValue(number);
                }
                if (field.getDataType().isNumeric()) {
                    String string = matcherGroup[2];
                    Integer number = Integer.parseInt(string);
                    searchCriteria.setValue(number);
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
        }
        return searchCriteria;
    }

    public List<Field<?>> getColumns(String tableName) {
        Table<?> foundTable = dslContext.meta().getTables(tableName).get(0);
        List<Field<?>> fields = Arrays.asList(foundTable.fields());
        LOG.info("My fields are " + fields);
        LOG.info("Data type id " + fields.get(0).getDataType());
        LOG.info("Name of date type id " + fields.get(0).getDataType().getTypeName());
        LOG.info("Data type name " + fields.get(1).getDataType());
        LOG.info("Data type param " + fields.get(6).getDataType());
        return fields;
    }
}