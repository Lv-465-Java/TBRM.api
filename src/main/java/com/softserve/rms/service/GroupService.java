package com.softserve.rms.service;

import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.dto.group.*;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public interface GroupService {
    Page<GroupDto> getAll(Integer page, Integer pageSize);

    GroupDto getById(Long id);

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
