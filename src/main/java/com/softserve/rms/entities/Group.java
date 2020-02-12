package com.softserve.rms.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    @OneToOne
    @JoinColumn(name = "owner_id")
    User owner;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "groups_members",
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private List<User> members;
}
