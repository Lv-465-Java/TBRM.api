package com.softserve.rms.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String tableName;

    private String description;

    @Column(name = "is_Published")
    private Boolean isPublished;

    @ManyToOne
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "resourceTemplate", cascade = CascadeType.REMOVE)
    private List<ResourceParameter> resourceParameters;

    @OneToMany(mappedBy = "relatedResourceTemplate")
    private List<ResourceRelation> resourceRelations;
}