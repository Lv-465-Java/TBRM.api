package com.softserve.rms.service;

import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.dto.group.*;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import org.springframework.data.domain.Page;

import java.security.Principal;

public interface GroupService {
    Page<GroupDto> getAll(Integer page, Integer pageSize);

    Page<MemberDto> getAllMembers(Long groupId, Integer page, Integer pageSize);

    GroupDto getByName(String name);

    GroupDto createGroup(GroupSaveDto groupSaveDto);

    MemberDto addMember(MemberOperationDto memberSaveDto);

    void addWritePermission(GroupPermissionDto groupPermissionDto, Principal principal);

    void changeGroupOwner(ChangeOwnerDto changeOwnerDto, Principal principal);

    void closePermissionForCertainUser(GroupPermissionDto groupPermissionDto, Principal principal);

    Page<PrincipalPermissionDto> findPrincipalWithAccessToGroup(Long id, Integer page, Integer pageSize);

    GroupDto update(String name, GroupSaveDto groupSaveDto);

    void deleteCroup(String groupName);

    void deleteMember(MemberOperationDto memberDeleteDto);
}
