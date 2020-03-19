package com.softserve.rms.repository;

import com.softserve.rms.entities.Group;
import com.softserve.rms.entities.GroupsMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupsMember, Long> {

    /**
     * Method deletes user from {@link Group}.
     *
     * @param userId user id
     * @param groupId group id
     */
    @Transactional
    @Modifying
    @Query(value = "delete from groups_members e where user_id = :userId and group_id = :groupId", nativeQuery = true)
    void deleteMember(Long userId, Long groupId);

    /**
     * Method deletes group by id.
     *
     * @param id of group
     */
    @Modifying
    @Query(value = "delete from groups_members where group_id = :id"
            , nativeQuery = true)
    void deleteByGroupId(Long id);

    /**
     * Method finds group member by ids.
     *
     * @param userId user id
     * @param groupId group id
     * @return {@link Optional<GroupsMember>}
     */
    @Query(value = "select * from groups_members where user_id = :userId and  group_id = :groupId", nativeQuery = true)
    Optional<GroupsMember> findOne(Long userId, Long groupId);
}