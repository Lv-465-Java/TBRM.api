package com.softserve.rms.constants;

public enum FieldConstants {
    ID("id"),
    NAME("name"),
    IS_PUBLISHED("isPublished"),
    DESCRIPTION("description"),
    RESOURCE_TEMPLATE_ID("resource_template_id"),
    USER_ID("user_id"),

    PRIMARY_KEY("_PK"),
    FOREIGN_KEY("_FK"),
    FROM("from"),
    TO("to"),
    REFERENCE("_ref");

    private String value;

    FieldConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}