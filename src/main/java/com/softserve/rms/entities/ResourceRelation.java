package com.softserve.rms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
//    @JsonIgnore
    @ManyToOne
    private ResourceParameter resourceParameter;
//    @JsonIgnore
    @ManyToOne
    private ResourceTemplate relatedResourceTemplate;



}