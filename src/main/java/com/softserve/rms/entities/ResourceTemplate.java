package com.softserve.rms.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "resource_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceTemplate {
    @Id
    @GeneratedValue(generator = "resource_templates_id_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "resource_templates_id_seq", sequenceName = "resource_templates_id_seq",
            allocationSize = 1)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "resourceTemplate")
    private Set<ResourceParameter> resourceParameters;
}