package com.softserve.rms.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "resource_relations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private ResourceParameter resourceParameter;
    @ManyToOne
    private ResourceTemplate relatedResourceTemplate;



}