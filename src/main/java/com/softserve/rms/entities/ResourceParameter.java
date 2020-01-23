package com.softserve.rms.entities;

import javax.persistence.*;

@Entity(name = "resource_parameters")
public class ResourceParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "type_name")
    private String typeName;
    @Column(name = "field_type")
    private String fieldType;
    @Column(name = "pattern")
    private String pattern;
    //private String tableName;
    @JoinColumn(name = "resource_id")
    @ManyToOne
    private ResourceTemplate resourceTemplate;
}