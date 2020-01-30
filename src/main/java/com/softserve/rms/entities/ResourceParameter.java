package com.softserve.rms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Enumerated(EnumType.STRING)
    private ParameterType parameterType;

    private String pattern;

    @JsonIgnore
    @ManyToOne
    private ResourceTemplate resourceTemplate;

    @JsonIgnore
    @OneToMany(mappedBy = "resourceParameter", cascade = CascadeType.REMOVE)
    private List<ResourceRelation> resourceRelations;
}