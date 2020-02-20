package com.softserve.rms.repository;

import com.softserve.rms.entities.Group;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAll();

    Optional<Group> findByName(String name);

    @PreAuthorize("hasRole('MANAGER')")
    Group save(Group group);

    @Modifying
    @Query(value = "update acl_sid set sid = :newName where sid = :oldName", nativeQuery = true)
    void updateAclSid(String oldName, String newName);

    void deleteByName(String name);

    @Query(value = "select mask from acl_entry where sid in (select id as sidId from acl_sid where sid = :userName)" +
            " and acl_object_identity in (select id from acl_object_identity where object_id_identity = :groupId)",
            nativeQuery = true)
    Integer getPermission(String userName, String groupId);
}
