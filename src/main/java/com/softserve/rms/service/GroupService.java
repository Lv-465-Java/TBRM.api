package com.softserve.rms.service;

import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.dto.group.*;
import com.softserve.rms.dto.security.ChangeOwnerDto;

import java.security.Principal;
import java.util.List;

public interface GroupService {
    List<GroupDto> getAll();

    GroupDto getByName(String name);

    GroupDto createGroup(GroupSaveDto groupSaveDto);

    MemberDto addMember(MemberOperationDto memberSaveDto);

    void addWritePermission(GroupPermissionDto groupPermissionDto, Principal principal);

    void changeGroupOwner(ChangeOwnerDto changeOwnerDto, Principal principal);

    void closePermissionForCertainUser(GroupPermissionDto groupPermissionDto, Principal principal);

    List<PrincipalPermissionDto> findPrincipalWithAccessToGroup(Long id);

    GroupDto update(String name, GroupSaveDto groupSaveDto);

    void deleteCroup(String groupName);

    void deleteMember(MemberOperationDto memberDeleteDto);
}
