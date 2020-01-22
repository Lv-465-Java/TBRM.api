package com.softserve.rms.entities;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings("ALL")
@Entity(name = "resource_templates")
public class ResourceTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "user_id")
    private Long userId;
    @OneToMany(mappedBy = "resource_parameter")
    private List<ResourceParameter> resourceParameters;
}