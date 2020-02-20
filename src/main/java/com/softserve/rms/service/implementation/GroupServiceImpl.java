package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.group.GroupDto;
import com.softserve.rms.dto.group.GroupSaveDto;
import com.softserve.rms.dto.group.MemberDto;
import com.softserve.rms.dto.group.MemberOperationDto;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import com.softserve.rms.entities.Group;
import com.softserve.rms.entities.GroupsMember;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueMemberException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.exceptions.PermissionException;
import com.softserve.rms.repository.GroupMemberRepository;
import com.softserve.rms.repository.GroupRepository;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.GroupService;
import com.softserve.rms.service.PermissionManagerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@PreAuthorize("hasRole('MANAGER')")
@Service
public class GroupServiceImpl  implements GroupService {
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private GroupMemberRepository groupMemberRepository;
    private PermissionManagerService permissionManagerService;
    private ModelMapper modelMapper;

    @Autowired
    public GroupServiceImpl(UserRepository userRepository, GroupRepository groupRepository,
                            GroupMemberRepository groupMemberRepository,
                            PermissionManagerService permissionManagerService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.permissionManagerService = permissionManagerService;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<GroupDto> getAll() {
        return groupRepository.findAll().stream()
                .map(group -> modelMapper.map(group, GroupDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public GroupDto getByName(String name) throws NotFoundException {
        return modelMapper.map(groupRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.GROUP_DO_NOT_EXISTS.getMessage())), GroupDto.class);
    }

    @Transactional
    @Override
    public GroupDto createGroup(GroupSaveDto groupSaveDto) throws NotFoundException, NotUniqueNameException {
        verifyIfGroupNameIsUnique(groupSaveDto.getName());
        Group newGroup = groupRepository.saveAndFlush(modelMapper.map(groupSaveDto, Group.class));
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        String mail = principal.getName();
        User user = userRepository.findUserByEmail(mail).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_DO_NOT_EXISTS.getMessage())
        );
        permissionManagerService.addPermission(new PermissionDto(newGroup.getId(), user.getEmail(), "write", true),
                principal, Group.class);
        return modelMapper.map(newGroup, GroupDto.class);
    }

    @Override
    public MemberDto addMember(MemberOperationDto memberSaveDto) throws NotFoundException, NotUniqueMemberException {
        Group group = groupRepository.findByName(memberSaveDto.getGroupName()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.GROUP_DO_NOT_EXISTS.getMessage())
        );
        User user = userRepository.findUserByEmail(memberSaveDto.getEmail()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_DO_NOT_EXISTS.getMessage())
        );
        verifyGroupPermission(group.getId().toString());
        verifyIfGroupMemberIsUnique(user.getId(), group.getId());
        GroupsMember groupsMember = new GroupsMember(user, group);
        groupMemberRepository.save(groupsMember);
        return new MemberDto(user.getEmail(), user.getFirstName(), user.getLastName());
    }

    @Override
    public void addWritePermission(PermissionDto permissionDto, Principal principal) {
        verifyGroupPermission(permissionDto.getId().toString());
        permissionManagerService.addPermission(permissionDto, principal, Group.class);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void changeGroupOwner(ChangeOwnerDto changeOwnerDto, Principal principal) {
        permissionManagerService.changeOwner(changeOwnerDto, principal, Group.class);
    }

    @Transactional
    @Override
    public GroupDto update(String name, GroupSaveDto groupSaveDto) throws NotFoundException, NotUniqueNameException {
        verifyIfGroupNameIsUnique(groupSaveDto.getName());
        Group group = groupRepository.findByName(name).orElseThrow(
                () -> new NotFoundException(ErrorMessage.GROUP_DO_NOT_EXISTS.getMessage())
        );
        verifyGroupPermission(group.getId().toString());
        if (groupSaveDto.getName() != null) {
            groupRepository.updateAclSid(group.getName(), groupSaveDto.getName());
            group.setName(groupSaveDto.getName());
        }
        if (groupSaveDto.getDescription() != null) {
            group.setDescription(groupSaveDto.getDescription());
        }
        groupRepository.save(group);
        return modelMapper.map(group, GroupDto.class);
    }

    @Transactional
    @Override
    public void deleteCroup(String groupName) throws NotFoundException {
        Group group = groupRepository.findByName(groupName).orElseThrow(
                () -> new NotFoundException(ErrorMessage.GROUP_DO_NOT_EXISTS.getMessage())
        );
        verifyGroupPermission(group.getId().toString());
        groupMemberRepository.deleteByGroupId(group.getId());
        groupRepository.deleteByName(groupName);
    }

    @Override
    public void deleteMember(MemberOperationDto memberDeleteDto) throws NotFoundException {
        Group group = groupRepository.findByName(memberDeleteDto.getGroupName()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.GROUP_DO_NOT_EXISTS.getMessage())
        );
        verifyGroupPermission(group.getId().toString());
        User user = userRepository.findUserByEmail(memberDeleteDto.getEmail()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_DO_NOT_EXISTS.getMessage())
        );
        groupMemberRepository.deleteMember(user.getId(), group.getId());
    }


    /**
     * Method verifies if {@link Group} name is unique.
     *
     * @param name of {@link GroupSaveDto}
     * @return string of {@link GroupSaveDto} name if it is unique
     * @throws NotUniqueNameException if the group name is not unique
     * @author Artur Sydor
     */
    private void verifyIfGroupNameIsUnique(String name) {
        if (groupRepository.findByName(name).isPresent()) {
            throw new NotUniqueNameException(ErrorMessage.GROUP_ALREADY_EXIST.getMessage());
        }
    }

    /**
     * Method verifies if {@link GroupsMember} name is unique.
     *
     * @param userId  of {@link User}
     * @param groupId of {@link Group}
     * @throws NotUniqueMemberException if user is member of group
     * @author Artur Sydor
     */
    private void verifyIfGroupMemberIsUnique(Long userId, Long groupId) {
        if (groupMemberRepository.findOne(userId, groupId).isPresent()) {
            throw new NotUniqueMemberException(ErrorMessage.GROUP_MEMBER_ALREADY_EXISTS.getMessage());
        }
    }

    private void verifyGroupPermission(String groupId) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        Integer mask = groupRepository.getPermission(principal.getName(), groupId);
        if (mask == null || mask != BasePermission.WRITE.getMask()) {
            throw new PermissionException(ErrorMessage.GROUP_ACCESS.getMessage());
        }
    }
}