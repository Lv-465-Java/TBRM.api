package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.group.GroupDto;
import com.softserve.rms.dto.group.GroupSaveDto;
import com.softserve.rms.dto.group.MemberDto;
import com.softserve.rms.dto.group.MemberOperationDto;
import com.softserve.rms.entities.Group;
import com.softserve.rms.entities.GroupsMember;
import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueMemberException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.exceptions.PermissionException;
import com.softserve.rms.repository.GroupMemberRepository;
import com.softserve.rms.repository.GroupRepository;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.PermissionManagerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GroupServiceImpl.class)
public class GroupServiceImplTest {
    private final String verifyGroupPermission = "verifyGroupPermission";
    private final String verifyIfGroupMemberIsUnique = "verifyIfGroupMemberIsUnique";
    private final String verifyIfGroupNameIsUnique = "verifyIfGroupNameIsUnique";

    @Mock
    private UserRepository userRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private GroupMemberRepository groupMemberRepository;
    @Mock
    private PermissionManagerService permissionManagerService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Principal principal;

    @InjectMocks
    private GroupServiceImpl groupService;

    private Group group;
    private List<Group> groups;
    private GroupDto groupDto;
    private GroupSaveDto groupSaveDto;
    private User user;
    private PermissionDto permissionDto;

    @Before
    public void init() {
        groupService = PowerMockito.spy(new GroupServiceImpl(userRepository, groupRepository,
                groupMemberRepository, permissionManagerService, modelMapper));
        group = new Group(1L, "group", "description", null);
        groups = Collections.singletonList(group);
        groupDto = new GroupDto(group.getName(), group.getDescription(), null);
        groupSaveDto = new GroupSaveDto("group", "");
        Role role = new Role(2L, "ROLE_MANAGER");
        user = new User(1L, "first", "last", "mail", "08000000000",
                "password", true, role, null, null);
        permissionDto = new PermissionDto(1L, "mail", "write", true);
    }

    @Test
    public void getAllOk() {
        doReturn(groups).when(groupRepository).findAll();
        doReturn(groupDto).when(modelMapper).map(any(Group.class), any(Class.class));
        List<GroupDto> actual = groupService.getAll();
        List<GroupDto> expected = Collections.singletonList(groupDto);
        assertEquals(actual, expected);
    }

    @Test
    public void getByNameOk() {
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doReturn(groupDto).when(modelMapper).map(any(Group.class), any(Class.class));
        GroupDto actual = groupService.getByName("");
        assertEquals(actual, groupDto);
    }

    @Test(expected = NotFoundException.class)
    public void getByNameFail() {
        doReturn(Optional.empty()).when(groupRepository).findByName(anyString());
        GroupDto actual = groupService.getByName("group");
    }

    @Test
    public void createGroupOk() throws Exception {
        String email = "mail";
        PowerMockito.doNothing().when(groupService, verifyIfGroupNameIsUnique, anyString());
        doReturn(group).when(modelMapper).map(any(GroupSaveDto.class), any(Class.class));
        doReturn(group).when(groupRepository).saveAndFlush(any(Group.class));
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        doReturn(email).when(authentication).getName();
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
        doNothing().when(permissionManagerService).addPermission(any(PermissionDto.class), any(Principal.class), any(Class.class));
        doReturn(groupDto).when(modelMapper).map(any(Group.class), any(Class.class));
        GroupDto actual = groupService.createGroup(groupSaveDto);
        assertEquals(actual, groupDto);
    }

    @Test(expected = NotUniqueNameException.class)
    public void createGroupNotUniqueName() throws Exception {
        doThrow(new NotUniqueNameException(ErrorMessage.GROUP_ALREADY_EXIST.getMessage()))
                .when(groupService, verifyIfGroupNameIsUnique, anyString());
//        doReturn(group).when(groupRepository).saveAndFlush(any(Group.class));
//        SecurityContextHolder.setContext(securityContext);
//        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
//        doReturn(Optional.of(user)).when(userRepository).findUserByEmail("mail");
//        doNothing().when(permissionManagerService).addPermission(any(PermissionDto.class), any(Principal.class), any(Class.class));
//        doReturn(groupDto).when(modelMapper).map(any(Group.class), any(Class.class));
        groupService.createGroup(new GroupSaveDto("group", ""));
    }

    @Test
    public void addMemberOk() throws Exception {
        MemberDto expected = new MemberDto(user.getEmail(), user.getFirstName(), user.getLastName());
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
        doNothing().when(groupService, verifyGroupPermission, anyString());
        doNothing().when(groupService, verifyIfGroupMemberIsUnique, anyLong(), anyLong());
        doReturn(new GroupsMember()).when(groupMemberRepository).save(any(GroupsMember.class));
        MemberOperationDto memberOperationDto = new MemberOperationDto("mail", "group");
        MemberDto actual = groupService.addMember(memberOperationDto);
        assertEquals(actual, expected);
    }

    @Test(expected = NotFoundException.class)
    public void addMemberGroupNotExist() {
        MemberDto expected = new MemberDto(user.getEmail(), user.getFirstName(), user.getLastName());
        MemberOperationDto memberOperationDto = new MemberOperationDto("mail", "group");
        doThrow(new NotFoundException(ErrorMessage.USER_DO_NOT_EXISTS.getMessage())).when(groupRepository).findByName(anyString());
//        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
//        PowerMockito.doNothing().when(groupService, verifyGroupPermission, anyString());
//        PowerMockito.doNothing().when(groupService, verifyIfGroupMemberIsUnique, anyLong(), anyLong());
//        doReturn(new GroupsMember()).when(groupMemberRepository).save(any(GroupsMember.class));
        groupService.addMember(memberOperationDto);
    }

    @Test(expected = NotFoundException.class)
    public void addMemberUserNotExist() {
        MemberDto expected = new MemberDto(user.getEmail(), user.getFirstName(), user.getLastName());
        MemberOperationDto memberOperationDto = new MemberOperationDto("mail", "group");
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doThrow(new NotFoundException(ErrorMessage.GROUP_DO_NOT_EXISTS.getMessage())).when(userRepository).findUserByEmail(anyString());
//        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
//        PowerMockito.doNothing().when(groupService, verifyGroupPermission, anyString());
//        PowerMockito.doNothing().when(groupService, verifyIfGroupMemberIsUnique, anyLong(), anyLong());
//        doReturn(new GroupsMember()).when(groupMemberRepository).save(any(GroupsMember.class));
        groupService.addMember(memberOperationDto);
    }

    @Test(expected = PermissionException.class)
    public void addMemberFailGroupPermission() throws Exception {
        MemberDto expected = new MemberDto(user.getEmail(), user.getFirstName(), user.getLastName());
        MemberOperationDto memberOperationDto = new MemberOperationDto("mail", "group");
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
        doThrow(new PermissionException(ErrorMessage.GROUP_ACCESS.getMessage())).when(groupService, verifyGroupPermission, anyString());
//        doNothing().when(groupService, verifyIfGroupMemberIsUnique, anyLong(), anyLong());
//        doReturn(new GroupsMember()).when(groupMemberRepository).save(any(GroupsMember.class));
        groupService.addMember(memberOperationDto);
    }

    @Test(expected = NotUniqueMemberException.class)
    public void addMemberNotUniqueMember() throws Exception {
        MemberDto expected = new MemberDto(user.getEmail(), user.getFirstName(), user.getLastName());
        MemberOperationDto memberOperationDto = new MemberOperationDto("mail", "group");
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
        doNothing().when(groupService, verifyGroupPermission, anyString());
        doThrow(new NotUniqueMemberException(ErrorMessage.GROUP_MEMBER_ALREADY_EXISTS.getMessage()))
                .when(groupService, verifyIfGroupMemberIsUnique, anyLong(), anyLong());
        //        doNothing().when(groupService, verifyIfGroupMemberIsUnique, anyLong(), anyLong());
//        doReturn(new GroupsMember()).when(groupMemberRepository).save(any(GroupsMember.class));
        groupService.addMember(memberOperationDto);
    }

    @Test
    public void addWritePermission() throws Exception {
        doNothing().when(groupService, verifyGroupPermission, anyString());
        doNothing().when(permissionManagerService).addPermission(any(PermissionDto.class), any(Principal.class), any(Class.class));
        groupService.addWritePermission(permissionDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void addWritePermissionIsNotOwner() throws Exception {
        doThrow(new PermissionException(ErrorMessage.GROUP_ACCESS.getMessage())).when(groupService, verifyGroupPermission, anyString());
        //doNothing().when(permissionManagerService).addPermission(any(PermissionDto.class), any(Principal.class), any(Class.class));
        groupService.addWritePermission(permissionDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void addWritePermissionAccessDenied() throws Exception {
        doNothing().when(groupService, verifyGroupPermission, anyString());
        doThrow(new PermissionException(ErrorMessage.ACCESS_DENIED.getMessage()))
                .when(permissionManagerService).addPermission(any(PermissionDto.class), any(Principal.class), any(Class.class));
        groupService.addWritePermission(permissionDto, principal);
    }

    @Test
    public void changeGroupOwner() {
    }

    @Test
    public void update() {

    }

    @Test
    public void deleteCroup() {
    }

    @Test
    public void deleteMember() {
    }
}