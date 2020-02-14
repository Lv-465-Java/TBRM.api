package com.softserve.rms.service;

import com.softserve.rms.dto.group.GroupDto;
import com.softserve.rms.dto.group.GroupSaveDto;
import com.softserve.rms.dto.group.MemberDto;
import com.softserve.rms.dto.group.MemberOperationDto;
import com.softserve.rms.entities.Group;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupService {
    List<GroupDto> getAll();

    GroupDto getByName(String name);

    GroupDto createGroup(GroupSaveDto groupSaveDto);

    MemberDto addMember(MemberOperationDto memberSaveDto);

    void update(Group group);

    void deleteCroup(String groupName);

    void deleteMember(MemberOperationDto memberDeleteDto);
}
