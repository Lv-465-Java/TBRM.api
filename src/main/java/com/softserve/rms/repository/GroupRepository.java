package com.softserve.rms.repository;

import com.softserve.rms.entities.Group;
import com.softserve.rms.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasRole('MANAGER')")
@Repository
public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {

    /**
     * Method finds all {@link Group}.
     *
     * @param pageable {@link Pageable}
     * @return {@link Page}
     */
    Page<Group> findAll(Pageable pageable);

    /**
     * Methods finds all members of group.
     *
     * @param groupId group id
     * @param pageable {@link Pageable}
     * @return {@link Page}
     */
    @Query("select g.user from GroupsMember g where g.group.id = ?1")
    Page<User> findAllMembers(Long groupId, Pageable pageable);

    /**
     * Methods finds all members of group
     *
     * @param name of group
     * @return list of users
     */
    @Query("select g.user from GroupsMember g where g.group.name = ?1")
    List<User> findAllMembers(String name);

    /**
     * Method find {@link Group} by name.
     *
     * @param name of group
     * @return {@link Optional<Group>}
     */
    Optional<Group> findByName(String name);

    /**
     * Methods for saving groups.
     *
     * @param group {@link Group}
     * @return {@link Group}
     */
    Group save(Group group);

    /**
     * Methods update acl sid for group.
     *
     * @param oldName of group
     * @param newName of group
     */
    @Modifying
    @Query(value = "update acl_sid set sid = :newName where sid = :oldName", nativeQuery = true)
    void updateAclSid(String oldName, String newName);

    /**
     * Methods deletes group by name.
     *
     * @param name of group
     */
    void deleteByName(String name);

    /**
     * Methods gets permissions for group.
     *
     * @param userName user email
     * @param groupId group id
     * @return {@link Integer} mask
     */
    @Query(value = "select mask from acl_entry where sid in (select id as sidId from acl_sid where sid = :userName)" +
            " and acl_object_identity in (select id from acl_object_identity where object_id_identity = :groupId)",
            nativeQuery = true)
    Integer getPermission(String userName, String groupId);
}
