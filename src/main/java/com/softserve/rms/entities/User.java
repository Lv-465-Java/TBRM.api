package com.softserve.rms.entities;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity(name = "users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EqualsAndHashCode(
        exclude = {"resourceTemplates"})

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column( nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false,unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 50)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = false;
//TODO mapping on roles table
//    @ManyToOne (fetch = FetchType.LAZY)
//    @JoinColumn(name = "role_id")
//    private Role role;
    @ToString.Exclude
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<ResourceTemplate> resourceTemplates;


}
