package com.softserve.rms.entities;

public enum ParameterType {
    POINT_INT("Point", "Integer"),
    POINT_DOUBLE("Point", "Double"),
    POINT_STRING("Point", "String"),
    RANGE_DOUBLE("Range", "Double"),
    RANGE_INT("Range", "Integer"),
    AREA_DOUBLE("Area", "Double");
//    LOCATION("...", "...");

    private String name;
    private String type;

    ParameterType(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
