package com.softserve.rms.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"resourceTemplates"})
@ToString(exclude = {"resourceTemplates"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column( nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false,unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String phone;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<ResourceTemplate> resourceTemplates;


}
