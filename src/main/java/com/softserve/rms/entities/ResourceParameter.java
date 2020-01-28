package com.softserve.rms.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "resource_parameters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String columnName;

    @Column(name = "field_type", nullable = false)
    private ParameterType parameterType;

    private String pattern;

    @ManyToOne
    private ResourceTemplate resourceTemplate;

    @OneToMany(mappedBy = "resourceParameter")
    private List<ResourceRelation> resourceRelations;
}