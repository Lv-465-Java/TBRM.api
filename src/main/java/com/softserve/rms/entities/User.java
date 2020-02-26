package com.softserve.rms.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"resourceTemplates"})
@ToString(exclude = {"resourceTemplates"})
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
    private boolean enabled;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    private String imageUrl;

    private String provider;

    private String providerId;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<ResourceTemplate> resourceTemplates;

    public User( String firstName, String email, Role role, String imageUrl, String provider, String providerId) {
        this.firstName=firstName;
        this.email=email;
        this.role=role;
        this.imageUrl=imageUrl;
        this.provider=provider;
        this.providerId=providerId;
    }
}
