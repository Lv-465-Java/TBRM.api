package com.softserve.rms.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "resource_parameters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "type_name", nullable = false)
    private String typeName;

    @Column(name = "field_type", nullable = false)
    private String fieldType;

    private String pattern;
//    private String tableName;

    @ManyToOne
    private ResourceTemplate resourceTemplate;
}