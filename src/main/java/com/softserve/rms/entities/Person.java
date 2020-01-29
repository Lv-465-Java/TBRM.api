package com.softserve.rms.entities;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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

public class Person implements Serializable, UserDetails {

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
    private Boolean status;
//TODO mapping on roles table
//    @ManyToOne (fetch = FetchType.LAZY)
//    @JoinColumn(name = "role_id")
    private String role;

    @OneToMany(mappedBy = "person", orphanRemoval = true)
    private List<ResourceTemplate> resourceTemplates;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE"+getRole()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
