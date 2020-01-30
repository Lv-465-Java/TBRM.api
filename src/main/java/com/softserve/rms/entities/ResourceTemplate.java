package com.softserve.rms.entities;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity(name = "resource_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(
//        exclude = {"person"})
//@ToString(
//        exclude = {"person"})
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

    private Boolean isPublished;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    @ToString.Exclude
    private Person person;

    @OneToMany(mappedBy = "resourceTemplate", cascade=CascadeType.REMOVE)
    private List<ResourceParameter> resourceParameters;

    @OneToMany(mappedBy = "relatedResourceTemplate")
    private List<ResourceRelation> resourceRelations;
}