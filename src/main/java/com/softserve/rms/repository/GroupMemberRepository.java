package com.softserve.rms.repository;

import com.softserve.rms.entities.GroupsMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupsMember, Long> {
    @Query(value = "delete from groups_members e where user_id = :userId and group_id = :groupId", nativeQuery = true)
    void deleteMember(Long userId, Long groupId);
}