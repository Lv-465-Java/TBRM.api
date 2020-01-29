package com.softserve.rms.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "resource_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String tableName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Person person;

    @OneToMany(mappedBy = "resourceTemplate")
    private List<ResourceParameter> resourceParameters;

    @OneToMany(mappedBy = "relatedResourceTemplate")
    private List<ResourceRelation> resourceRelations;
}