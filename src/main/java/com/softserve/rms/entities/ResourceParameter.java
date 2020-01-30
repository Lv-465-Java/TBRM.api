package com.softserve.rms.entities;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "resource_parameters")
@Data
public class ResourceParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String name;

    private String typeName;

    private String fieldType;

    private String pattern;

    private String tableName;

    @ManyToOne
    private ResourceTemplate resourceTemplate;
}