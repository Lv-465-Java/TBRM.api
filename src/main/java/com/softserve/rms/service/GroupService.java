package com.softserve.rms.service;

import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.group.GroupDto;
import com.softserve.rms.dto.group.GroupSaveDto;
import com.softserve.rms.dto.group.MemberDto;
import com.softserve.rms.dto.group.MemberOperationDto;
import com.softserve.rms.dto.security.ChangeOwnerDto;

import java.security.Principal;
import java.util.List;

public interface GroupService {
    List<GroupDto> getAll();

    GroupDto getByName(String name);

    GroupDto createGroup(GroupSaveDto groupSaveDto);

    MemberDto addMember(MemberOperationDto memberSaveDto);

    void addWritePermission(PermissionDto permissionDto, Principal principal);

    void changeGroupOwner(ChangeOwnerDto changeOwnerDto, Principal principal);

    GroupDto update(String name, GroupSaveDto groupSaveDto);

    void deleteCroup(String groupName);

    void deleteMember(MemberOperationDto memberDeleteDto);
}
