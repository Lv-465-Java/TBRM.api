package com.softserve.rms.repository;

import com.softserve.rms.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAll();

    Optional<Group> findByName(String name);

    Group save(Group group);

    @Modifying
    @Query(value = "update acl_sid set principal = :newName where principal = :oldName", nativeQuery = true)
    void updateAclSid(String oldName, String newName);

    void deleteByName(String name);
}
