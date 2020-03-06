package com.softserve.rms.constants;

public enum FieldConstants {
    ID("id"),
    NAME("name"),
    IS_PUBLISHED("isPublished"),
    DESCRIPTION("description"),
    RESOURCE_TEMPLATE_ID("resource_template_id"),
    USER_ID("user_id"),
    PHOTOS_NAMES("photos_names"),

    PRIMARY_KEY("_PK"),
    FOREIGN_KEY("_FK"),
    FROM("_from"),
    TO("_to"),
    REFERENCE("_ref"),
    RESOURCE_TEMPLATES_TABLE("resource_templates"),
    RESOURCE_RELATION_TABLE("resource_relations");

    private String value;

    FieldConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}