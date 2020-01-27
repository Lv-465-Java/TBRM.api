package com.softserve.rms.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "persons")
@EqualsAndHashCode(
      exclude = {"resource_templates"})
@ToString(
     exclude = {"resource_templates"})

public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name ="first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name ="last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false,unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String phone;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private String status;
//TODO mapping on roles table
//    @ManyToOne (fetch = FetchType.LAZY)
//    @JoinColumn(name = "role_id")
//    private Role role;

    @OneToMany(mappedBy = "person", orphanRemoval = true)
    private List<ResourceTemplate> resource_templates;


}
