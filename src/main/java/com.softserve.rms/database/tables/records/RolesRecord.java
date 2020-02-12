/*
 * This file is generated by jOOQ.
 */
package database.tables.records;


import database.tables.Roles;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
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
public class RolesRecord extends UpdatableRecordImpl<RolesRecord> implements Record2<Long, String> {

    private static final long serialVersionUID = 1312675476;

    /**
     * Setter for <code>public.roles.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.roles.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.roles.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.roles.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<Long, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Long, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Roles.ROLES.ID;
    }

    @Override
    public Field<String> field2() {
        return Roles.ROLES.NAME;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public RolesRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public RolesRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public RolesRecord values(Long value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RolesRecord
     */
    public RolesRecord() {
        super(Roles.ROLES);
    }

    /**
     * Create a detached, initialised RolesRecord
     */
    public RolesRecord(Long id, String name) {
        super(Roles.ROLES);

        set(0, id);
        set(1, name);
    }
}
