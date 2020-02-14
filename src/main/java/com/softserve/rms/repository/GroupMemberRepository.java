package com.softserve.rms.repository;

import com.softserve.rms.dto.group.MemberOperationDto;
import com.softserve.rms.entities.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    GroupMember save(Long userId, Long groupId);

    //void delete(GroupMember groupMember);

}
