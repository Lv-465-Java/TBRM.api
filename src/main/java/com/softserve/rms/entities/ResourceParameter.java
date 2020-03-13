package com.softserve.rms.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "resource_parameters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"resourceTemplate"})
public class ResourceParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String columnName;

    @Column(name = "field_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParameterType parameterType;

    private String pattern;

    @ManyToOne
    private ResourceTemplate resourceTemplate;

//    @ManyToOne
//    private Resource resource;

    @OneToOne(mappedBy = "resourceParameter", cascade = CascadeType.REMOVE)
    private ResourceRelation resourceRelations;
}
