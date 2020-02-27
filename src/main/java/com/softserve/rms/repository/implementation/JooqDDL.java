package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.entities.*;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.impl.SQLDataType;

import java.util.List;

import static org.jooq.impl.DSL.constraint;

public class JooqDDL {
    private DSLContext dslContext;

    public JooqDDL(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * Method creates {@link ResourceRecord} container table based on {@link ResourceTemplate} table name
     * with static columns.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    public void createResourceContainerTable(ResourceTemplate resourceTemplate) {
        dslContext.createTable(resourceTemplate.getTableName())
                .column(FieldConstants.ID.getValue(), SQLDataType.BIGINT.nullable(false).identity(true))
                .column(FieldConstants.NAME.getValue(), SQLDataType.VARCHAR(255).nullable(false))
                .column(FieldConstants.DESCRIPTION.getValue(), SQLDataType.VARCHAR(255))
                .column(FieldConstants.RESOURCE_TEMPLATE_ID.getValue(), SQLDataType.BIGINT.nullable(false))
                .column(FieldConstants.USER_ID.getValue(), SQLDataType.BIGINT.nullable(false))
                .constraints(
                        constraint(resourceTemplate.getTableName()
                                .concat(FieldConstants.PRIMARY_KEY.getValue()))
                                .primaryKey(FieldConstants.ID.getValue()),
                        constraint(FieldConstants.RESOURCE_TEMPLATE_ID.getValue()
                                .concat(FieldConstants.FOREIGN_KEY.getValue()))
                                .foreignKey(FieldConstants.RESOURCE_TEMPLATE_ID.getValue())
                                .references(FieldConstants.RESOURCE_TEMPLATES_TABLE.getValue(), FieldConstants.ID.getValue()))
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
     * Method alters {@link ResourceRecord} container table and adds a new column based on Point or Coordinate parameter types.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @param parameter        {@link ResourceParameter}
     * @author Halyna Yatseniuk
     */
    private void addColumnWithPointOrCoordinatesParameterType(ResourceTemplate resourceTemplate,
                                                              ResourceParameter parameter) {
        dslContext.alterTable(resourceTemplate.getTableName())
                .addColumn(parameter.getColumnName(), parameter.getParameterType().getSqlType().nullable(false))
                .execute();
    }

    /**
     * Method alters {@link ResourceRecord} container table and adds new columns based on Range parameter type.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @param parameter        {@link ResourceParameter}
     * @author Halyna Yatseniuk
     */
    private void addColumnsWithRangeParameterType(ResourceTemplate resourceTemplate, ResourceParameter parameter) {
        dslContext.alterTable(resourceTemplate.getTableName())
                .addColumn(parameter.getColumnName().concat(FieldConstants.FROM.getValue()),
                        parameter.getParameterType().getSqlType().nullable(false))
                .execute();
        dslContext.alterTable(resourceTemplate.getTableName())
                .addColumn(parameter.getColumnName().concat(FieldConstants.TO.getValue()),
                        parameter.getParameterType().getSqlType().nullable(false))
                .execute();
    }

    /**
     * Method alters {@link ResourceRecord} container table and adds a new column based on Point Reference parameter type.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @param parameter        {@link ResourceParameter}
     * @author Halyna Yatseniuk
     */
    private void addColumnWithPointReferenceParameterType(ResourceTemplate resourceTemplate,
                                                          ResourceParameter parameter) {
        ResourceRelation resourceRelation = parameter.getResourceRelations();
        dslContext.alterTable(resourceTemplate.getTableName())
                .addColumn(parameter.getColumnName().concat(FieldConstants.REFERENCE.getValue()),
                        parameter.getParameterType().getSqlType().nullable(true))
                .execute();
        addConstraint(resourceTemplate, parameter, resourceRelation);
    }

    /**
     * Method alters {@link ResourceRecord} container table and adds a new constraint foreign key based on
     * Point Reference parameter type.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @param parameter        {@link ResourceParameter}
     * @param resourceRelation {@link ResourceRelation}
     * @author Halyna Yatseniuk
     */
    private void addConstraint(ResourceTemplate resourceTemplate,
                               ResourceParameter parameter, ResourceRelation resourceRelation) {
        dslContext.alterTable(resourceTemplate.getTableName())
                .add(constraint(parameter.getColumnName().concat(FieldConstants.FOREIGN_KEY.getValue()))
                        .foreignKey(parameter.getColumnName().concat(FieldConstants.REFERENCE.getValue()))
                        .references(resourceRelation.getRelatedResourceTemplate().getTableName()))
                .execute();
    }

    /**
     * Method counts {@link ResourceRecord} container table records amount.
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
     * Method checks whether amount of references to {@link ResourceRecord} container table is zero.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    public Boolean countReferencesToTable(ResourceTemplate resourceTemplate) {
        Table<?> foundTable = dslContext.meta().getTables(resourceTemplate.getTableName()).get(0);
        long referencesAmount = dslContext
                .meta().getTables().stream()
                .map(table -> table.getReferencesTo((foundTable)))
                .filter(size -> !(size.isEmpty())).count();
        return !(referencesAmount == 0);
    }

    /**
     * Method drops {@link ResourceRecord} container table.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    public void dropResourceContainerTable(ResourceTemplate resourceTemplate) {
        dslContext.dropTable(resourceTemplate.getTableName())
                .execute();
    }
}
