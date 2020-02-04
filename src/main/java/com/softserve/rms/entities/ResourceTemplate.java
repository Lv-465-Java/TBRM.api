package com.softserve.rms.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "resource_templates")
@Data
public class ResourceTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    private Person person;

    @OneToMany(mappedBy = "resourceTemplate")
    private List<ResourceParameter> resourceParameters;
}
