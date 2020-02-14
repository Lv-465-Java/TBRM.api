package com.softserve.rms.repository;

import com.softserve.rms.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAll();

    Group findByName(String name);

    Group save(Group group);

    void deleteByName(String name);
}
