package com.softserve.rms.entities;

public enum ParameterType {
    POINT_INT("Point", "Integer", "integer"),
    POINT_DOUBLE("Point", "Double", "numeric"),
    POINT_STRING("Point", "String", "varchar"),
    RANGE_DOUBLE("Range", "Double", "numeric"),
    RANGE_INT("Range", "Integer", "integer"),
    AREA_DOUBLE("Area", "Double", "numeric");

    private String name;
    private String type;
    private String sqlType;

    ParameterType(String name, String type, String sqlType) {
        this.name = name;
        this.type = type;
        this.sqlType = sqlType;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getSqlType() {
        return sqlType;
    }
}