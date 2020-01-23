package com.softserve.rms.entities;

import javax.persistence.*;
import java.util.List;

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
    //TODO mapping on users table
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "resourceTemplate")
    private List<ResourceParameter> resourceParameters;
}