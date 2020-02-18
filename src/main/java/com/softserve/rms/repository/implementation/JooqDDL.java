package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.entities.*;
import org.jooq.DSLContext;
import org.jooq.impl.SQLDataType;

import java.util.List;

import static org.jooq.impl.DSL.constraint;

public class JooqDDL {
    private DSLContext dslContext;

    public JooqDDL(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * Method creates {@link Resource} container table based on {@link ResourceTemplate} table name
     * with static columns.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
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

    /**
     * Method checks {@link ResourceTemplate} parameter types and invokes appropriate for type alter table method.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
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

    /**
     * Method alters {@link Resource} container table and adds a new column based on Point or Coordinate parameter types.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    private void addColumnWithPointOrCoordinatesParameterType(ResourceTemplate resourceTemplate,
                                                              ResourceParameter parameter) {
        dslContext.alterTable(resourceTemplate.getTableName())
                .addColumn(parameter.getColumnName(), parameter.getParameterType().getSqlType().nullable(false))
                .execute();
    }

    /**
     * Method alters {@link Resource} container table and adds new columns based on Range parameter type.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
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

    /**
     * Method alters {@link Resource} container table and adds a new column with constraint foreign key based on
     * Point Reference parameter type.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    private void addColumnWithPointReferenceParameterType(ResourceTemplate resourceTemplate,
                                                          ResourceParameter parameter) {
        ResourceRelation resourceRelation = parameter.getResourceRelations();
        dslContext.alterTable(resourceTemplate.getTableName())
                .addColumn(parameter.getColumnName().concat("_ref"),
                        parameter.getParameterType().getSqlType().nullable(true))
                .execute();
        dslContext.alterTable(resourceTemplate.getTableName())
                .add(constraint(parameter.getColumnName().concat("_FK"))
                        .foreignKey("id").references(resourceRelation.getRelatedResourceTemplate().getTableName()))
                .execute();
    }

    /**
     * Method counts {@link Resource} container table records amount.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    public int countTableRecords(ResourceTemplate resourceTemplate) {
        return dslContext.selectCount()
                .from(resourceTemplate.getTableName())
                .fetchOne(0, int.class);
    }

    /**
     * Method drops {@link Resource} container table.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    public void dropResourceContainerTable(ResourceTemplate resourceTemplate) {
        dslContext.dropTable(resourceTemplate.getTableName())
                .execute();
    }
}