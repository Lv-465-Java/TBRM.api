package com.softserve.rms.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "groups_members", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "group_id"}))
public class GroupsMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;
}
