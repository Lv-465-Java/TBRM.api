package com.softserve.rms.entities;

import lombok.Data;

import javax.persistence.*;

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
}
