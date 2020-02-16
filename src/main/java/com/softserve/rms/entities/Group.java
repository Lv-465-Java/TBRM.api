package com.softserve.rms.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "groups_members",
            joinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private List<User> users;
}
