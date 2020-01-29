package com.softserve.rms.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity(name = "persons")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "persons")
@EqualsAndHashCode(
        exclude = {"resourceTemplates"})
@ToString(
        exclude = {"resourceTemplates"})

public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String phone;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = false;
//TODO mapping on roles table
//    @ManyToOne (fetch = FetchType.EAGER)
//    private Role role;

    @OneToMany(mappedBy = "person", orphanRemoval = true)
    private List<ResourceTemplate> resourceTemplates;


}
