package com.softserve.rms.repository;

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
    @Transactional
    @Modifying
    @Query(value = "delete from groups_members e where user_id = :userId and group_id = :groupId", nativeQuery = true)
    void deleteMember(Long userId, Long groupId);

    @Modifying
    @Query(value = "delete from groups_members where group_id = :id"
            , nativeQuery = true)
    void deleteByGroupId(Long id);

    @Query(value = "select * from groups_members where user_id = :userId and  group_id = :groupId", nativeQuery = true)
    Optional<GroupsMember> findOne(Long userId, Long groupId);


}