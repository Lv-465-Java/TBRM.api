package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.entities.ParameterType;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceTemplate;
import org.jooq.DSLContext;
import org.jooq.impl.SQLDataType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.jooq.impl.DSL.constraint;

public class JooqDDL {
    private DSLContext dslContext;

    public JooqDDL(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Transactional
    public void createResourceContainerTable(ResourceTemplate resourceTemplate) {
        dslContext.createTable(resourceTemplate.getTableName())
                .column(FieldConstants.ID.getValue(), SQLDataType.BIGINT.nullable(false))
                .column(FieldConstants.NAME.getValue(), SQLDataType.VARCHAR(255).nullable(false))
                .column(FieldConstants.DESCRIPTION.getValue(), SQLDataType.VARCHAR(255))
                .column(FieldConstants.RESOURCE_TEMPLATE_ID.getValue(), SQLDataType.BIGINT.nullable(false))
                .column(FieldConstants.USER_ID.getValue(), SQLDataType.BIGINT.nullable(false))
                .constraints(constraint(resourceTemplate.getTableName().concat("_PK"))
                        .primaryKey(FieldConstants.ID.getValue()))
                .execute();
        addColumnsToResourceContainerTable(resourceTemplate);
    }

    private void addColumnsToResourceContainerTable(ResourceTemplate resourceTemplate) {
        List<ResourceParameter> resourceParameterList = resourceTemplate.getResourceParameters();
        for (ResourceParameter parameter : resourceParameterList) {
            if (parameter.getParameterType().equals(ParameterType.POINT_INT) ||
                    parameter.getParameterType().equals(ParameterType.POINT_DOUBLE) ||
                    parameter.getParameterType().equals(ParameterType.POINT_STRING) ||
                    parameter.getParameterType().equals(ParameterType.COORDINATES)) {
                addColumnWithPointOrCoordinatesParameterType(resourceTemplate, parameter);
            } else if (parameter.getParameterType().equals(ParameterType.RANGE_INT) ||
                    parameter.getParameterType().equals(ParameterType.RANGE_DOUBLE)) {
                addColumnsWithRangeParameterType(resourceTemplate, parameter);
            } else if (parameter.getParameterType().equals(ParameterType.POINT_REFERENCE)) {
                addColumnWithPointReferenceParameterType(resourceTemplate, parameter);
            }
        }
    }

    private void addColumnWithPointOrCoordinatesParameterType(ResourceTemplate resourceTemplate,
                                                              ResourceParameter parameter) {
        dslContext.alterTable(resourceTemplate.getTableName())
                .addColumn(parameter.getColumnName(), parameter.getParameterType().getSqlType().nullable(false))
                .execute();
    }

    private void addColumnsWithRangeParameterType(ResourceTemplate resourceTemplate, ResourceParameter parameter) {
        dslContext.alterTable(resourceTemplate.getTableName())
                .addColumn(parameter.getColumnName().concat("_from"),
                        parameter.getParameterType().getSqlType().nullable(false))
                .execute();
        dslContext.alterTable(resourceTemplate.getTableName())
                .addColumn(parameter.getColumnName().concat("_to"),
                        parameter.getParameterType().getSqlType().nullable(false))
                .execute();
    }

    private void addColumnWithPointReferenceParameterType(ResourceTemplate resourceTemplate,
                                                          ResourceParameter parameter) {
        dslContext.alterTable(resourceTemplate.getTableName())
                .addColumn(parameter.getColumnName().concat("_ref"),
                        parameter.getParameterType().getSqlType().nullable(true))
                .execute();
    }
}