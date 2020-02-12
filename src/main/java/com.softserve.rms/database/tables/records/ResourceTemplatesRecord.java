/*
 * This file is generated by jOOQ.
 */
package database.tables.records;


import database.tables.ResourceTemplates;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ResourceTemplatesRecord extends UpdatableRecordImpl<ResourceTemplatesRecord> implements Record6<Long, String, Boolean, String, String, Long> {

    private static final long serialVersionUID = -863051303;

    /**
     * Setter for <code>public.resource_templates.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.resource_templates.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.resource_templates.description</code>.
     */
    public void setDescription(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.resource_templates.description</code>.
     */
    public String getDescription() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.resource_templates.is_published</code>.
     */
    public void setIsPublished(Boolean value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.resource_templates.is_published</code>.
     */
    public Boolean getIsPublished() {
        return (Boolean) get(2);
    }

    /**
     * Setter for <code>public.resource_templates.name</code>.
     */
    public void setName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.resource_templates.name</code>.
     */
    public String getName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.resource_templates.table_name</code>.
     */
    public void setTableName(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.resource_templates.table_name</code>.
     */
    public String getTableName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>public.resource_templates.creator_id</code>.
     */
    public void setCreatorId(Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.resource_templates.creator_id</code>.
     */
    public Long getCreatorId() {
        return (Long) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row6<Long, String, Boolean, String, String, Long> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    public Row6<Long, String, Boolean, String, String, Long> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return ResourceTemplates.RESOURCE_TEMPLATES.ID;
    }

    @Override
    public Field<String> field2() {
        return ResourceTemplates.RESOURCE_TEMPLATES.DESCRIPTION;
    }

    @Override
    public Field<Boolean> field3() {
        return ResourceTemplates.RESOURCE_TEMPLATES.IS_PUBLISHED;
    }

    @Override
    public Field<String> field4() {
        return ResourceTemplates.RESOURCE_TEMPLATES.NAME;
    }

    @Override
    public Field<String> field5() {
        return ResourceTemplates.RESOURCE_TEMPLATES.TABLE_NAME;
    }

    @Override
    public Field<Long> field6() {
        return ResourceTemplates.RESOURCE_TEMPLATES.CREATOR_ID;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getDescription();
    }

    @Override
    public Boolean component3() {
        return getIsPublished();
    }

    @Override
    public String component4() {
        return getName();
    }

    @Override
    public String component5() {
        return getTableName();
    }

    @Override
    public Long component6() {
        return getCreatorId();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getDescription();
    }

    @Override
    public Boolean value3() {
        return getIsPublished();
    }

    @Override
    public String value4() {
        return getName();
    }

    @Override
    public String value5() {
        return getTableName();
    }

    @Override
    public Long value6() {
        return getCreatorId();
    }

    @Override
    public ResourceTemplatesRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public ResourceTemplatesRecord value2(String value) {
        setDescription(value);
        return this;
    }

    @Override
    public ResourceTemplatesRecord value3(Boolean value) {
        setIsPublished(value);
        return this;
    }

    @Override
    public ResourceTemplatesRecord value4(String value) {
        setName(value);
        return this;
    }

    @Override
    public ResourceTemplatesRecord value5(String value) {
        setTableName(value);
        return this;
    }

    @Override
    public ResourceTemplatesRecord value6(Long value) {
        setCreatorId(value);
        return this;
    }

    @Override
    public ResourceTemplatesRecord values(Long value1, String value2, Boolean value3, String value4, String value5, Long value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached ResourceTemplatesRecord
     */
    public ResourceTemplatesRecord() {
        super(ResourceTemplates.RESOURCE_TEMPLATES);
    }

    /**
     * Create a detached, initialised ResourceTemplatesRecord
     */
    public ResourceTemplatesRecord(Long id, String description, Boolean isPublished, String name, String tableName, Long creatorId) {
        super(ResourceTemplates.RESOURCE_TEMPLATES);

        set(0, id);
        set(1, description);
        set(2, isPublished);
        set(3, name);
        set(4, tableName);
        set(5, creatorId);
    }
}
