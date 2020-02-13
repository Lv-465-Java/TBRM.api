package com.softserve.rms.repository.implementation;

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
                .column("id", SQLDataType.BIGINT.nullable(false))
                .column("name", SQLDataType.VARCHAR(255).nullable(false))
                .column("description", SQLDataType.VARCHAR(255))
                .column("resource_template_id", SQLDataType.BIGINT.nullable(false))
                .column("user_id", SQLDataType.BIGINT.nullable(false))
                .constraints(constraint("PK_ID").primaryKey("id"))
                .execute();
        addColumnsToResourceContainerTable(resourceTemplate);
    }

    @Transactional
    public void addColumnsToResourceContainerTable(ResourceTemplate resourceTemplate) {
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
            }
        }
    }

    private void addColumnWithPointOrCoordinatesParameterType(ResourceTemplate resourceTemplate,
                                                              ResourceParameter parameter) {
        if (parameter.getResourceRelations() == null) {
            dslContext.alterTable(resourceTemplate.getTableName())
                    .addColumn(parameter.getColumnName(), parameter.getParameterType().getSqlType().nullable(false))
                    .execute();
        } else {
            dslContext.alterTable(resourceTemplate.getTableName())
                    .addColumn(parameter.getColumnName().concat("_ref"),
                            parameter.getParameterType().getSqlType().nullable(true))
                    .execute();
        }
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
}