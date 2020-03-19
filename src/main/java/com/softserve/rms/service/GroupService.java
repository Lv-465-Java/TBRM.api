package com.softserve.rms.service;

import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.dto.group.*;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import com.softserve.rms.entities.Group;
import org.springframework.data.domain.Page;

import java.security.Principal;


public interface GroupService {
    /**
     * Method finds all {@link Group}.
     *
     * @param page current page
     * @param pageSize elements per page
     * @return all {@link Group}
     * @author Artur Sydor
     */
    Page<GroupDto> getAll(Integer page, Integer pageSize);

    /**
     * Method finds {@link Group} by ID.
     *
     * @return {@link Group}
     * @author Artur Sydor
     */
    GroupDto getById(Long id);

    /**
     * Method finds all group members by group id.
     *
     * @param groupId group id
     * @param page current page
     * @param pageSize elements per page
     * @return all group members
     * @author Artur Sydor
     */
    Page<MemberDto> getAllMembers(Long groupId, Integer page, Integer pageSize);

    /**
     * Method finds group by name.
     *
     * @param name of group
     * @return {@link GroupDto}
     * @author Artur Sydor
     */
    GroupDto getByName(String name);

    /**
     * Method for saving new group.
     *
     * @param groupSaveDto group parameters
     * @return saved group
     * @author Artur Sydor
     */
    GroupDto createGroup(GroupSaveDto groupSaveDto);

    /**
     * Methods save new user in group.
     *
     * @param memberSaveDto user parameters
     * @return saved user
     * @author Artur Sydor
     */
    MemberDto addMember(MemberOperationDto memberSaveDto);

    /**
     * Methods for adding write permissions on editing certain group.
     *
     * @param groupPermissionDto parameters needed to add permission
     * @param principal authenticated user
     * @author Artur Sydor
     */
    void addWritePermission(GroupPermissionDto groupPermissionDto, Principal principal);

    /**
     * Method for changing group owner.
     *
     * @param changeOwnerDto parameters needed to change owner
     * @param principal authenticated user
     * @author Artur Sydor
     */
    void changeGroupOwner(ChangeOwnerDto changeOwnerDto, Principal principal);

    /**
     * Method for deleting permissions on group.
     *
     * @param groupPermissionDto parameters needed to add permission
     * @param principal authenticated user
     * @author Artur Sydor
     */
    void closePermissionForCertainUser(GroupPermissionDto groupPermissionDto, Principal principal);

    /**
     * Method finds all users with access to group.
     *
     * @param id group id
     * @param page current page
     * @param pageSize elements per page
     * @return all users with access to group
     * @author Artur Sydor
     */
    Page<PrincipalPermissionDto> findPrincipalWithAccessToGroup(Long id, Integer page, Integer pageSize);

    /**
     * Methods for updating group.
     *
     * @param name group name
     * @param groupSaveDto parameters for update
     * @return updated group dto
     * @author Artur Sydor
     */
    GroupDto update(String name, GroupSaveDto groupSaveDto);

    /**
     * Method deletes group by name.
     *
     * @param groupName name of group
     * @author Artur Sydor
     */
    void deleteCroup(String groupName);

    /**
     * Methods deletes user from group.
     *
     * @param memberDeleteDto user parameters
     * @author Artur Sydor
     */
    void deleteMember(MemberOperationDto memberDeleteDto);
}
