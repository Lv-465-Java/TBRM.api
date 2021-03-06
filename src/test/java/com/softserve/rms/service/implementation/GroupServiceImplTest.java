package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.dto.group.*;
import com.softserve.rms.dto.security.ChangeOwnerDto;
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
import com.softserve.rms.util.EmailSender;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.data.domain.*;
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
@PrepareForTest({GroupServiceImpl.class, PageRequest.class})
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
    @Mock
    private Page<Group> page;
    @Mock
    private EmailSender emailSender;

    @InjectMocks
    private GroupServiceImpl groupService;

    private Role role = new Role(2L, "ROLE_MANAGER");
    private User user = new User(1L, "first", "last", "mail", "08000000000",
            "password", true, role,"imageUrl","google","3145262", Collections.emptyList(), "resetToken", Collections.emptyList());
    private Group group = new Group(1L, "group", "description");
    private GroupsMember groupsMember = new GroupsMember(1L, user, group);
    private List<Group> groups = Collections.singletonList(group);
    private GroupDto groupDto = new GroupDto(1L, group.getName(), group.getDescription());
    private GroupSaveDto groupSaveDto = new GroupSaveDto("group", "");
    private PermissionDto permissionDto = new PermissionDto(1L, "mail", "write", true);
    private MemberOperationDto memberOperationDto = new MemberOperationDto("mail", "group");
    private ChangeOwnerDto changeOwnerDto = new ChangeOwnerDto(1L, "recipient");
    private GroupPermissionDto groupPermissionDto = new GroupPermissionDto(1L, "recipient");

    @Before
    public void init() {
        groupService = PowerMockito.spy(new GroupServiceImpl(userRepository, groupRepository,
                groupMemberRepository, permissionManagerService, modelMapper, emailSender));
        page = PowerMockito.mock(Page.class);
    }

    @Test
    public void getAllOk() {
        Page<GroupDto> expected = new PageImpl<>(Collections.singletonList(groupDto));
        doReturn(page).when(groupRepository).findAll(any(Pageable.class));
        doReturn(expected).when(page).map(any());
        Page<GroupDto> actual = groupService.getAll(1, 5);
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
        groupService.getByName("group");
    }

    @Test
    public void createGroupOk() throws Exception {
        String email = "mail";
        doNothing().when(groupService, verifyIfGroupNameIsUnique, anyString());
        doReturn(group).when(modelMapper).map(any(GroupSaveDto.class), any(Class.class));
        doReturn(group).when(groupRepository).save(any(Group.class));
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        doReturn(email).when(authentication).getName();
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
        doNothing().when(permissionManagerService).addPermission(any(PermissionDto.class), any(Principal.class), any(Class.class));
        doReturn(groupDto).when(modelMapper).map(any(Group.class), any(Class.class));
        GroupDto actual = groupService.createGroup(groupSaveDto);
        assertEquals(actual, groupDto);
    }

    @Test(expected = NotFoundException.class)
    public void createGroupUserNotExist() throws Exception {
        String email = "mail";
        doNothing().when(groupService, verifyIfGroupNameIsUnique, anyString());
        doReturn(group).when(modelMapper).map(any(GroupSaveDto.class), any(Class.class));
        doReturn(group).when(groupRepository).save(any(Group.class));
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        doReturn(email).when(authentication).getName();
        doReturn(Optional.empty()).when(userRepository).findUserByEmail(anyString());
        groupService.createGroup(groupSaveDto);
    }
    @Test(expected = RuntimeException.class)
    public void createGroupNotValidName() {
        groupService.createGroup(new GroupSaveDto());
    }

    @Test(expected = NotUniqueNameException.class)
    public void createGroupNotUniqueName() throws Exception {
        doThrow(new NotUniqueNameException(ErrorMessage.GROUP_ALREADY_EXIST.getMessage()))
                .when(groupService, verifyIfGroupNameIsUnique, anyString());
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
        MemberDto actual = groupService.addMember(memberOperationDto);
        assertEquals(actual, expected);
    }

    @Test(expected = NotFoundException.class)
    public void addMemberGroupNotExist() {
        doReturn(Optional.empty()).when(groupRepository).findByName(anyString());
        groupService.addMember(memberOperationDto);
    }

    @Test(expected = NotFoundException.class)
    public void addMemberUserNotExist() {
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doReturn(Optional.empty()).when(userRepository).findUserByEmail(anyString());
        groupService.addMember(memberOperationDto);
    }

    @Test(expected = PermissionException.class)
    public void addMemberFailGroupPermission() throws Exception {
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
        doThrow(new PermissionException(ErrorMessage.GROUP_ACCESS.getMessage())).when(groupService, verifyGroupPermission, anyString());
        groupService.addMember(memberOperationDto);
    }

    @Test(expected = NotUniqueMemberException.class)
    public void addMemberNotUniqueMember() throws Exception {
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
        doNothing().when(groupService, verifyGroupPermission, anyString());
        doThrow(new NotUniqueMemberException(ErrorMessage.GROUP_MEMBER_ALREADY_EXISTS.getMessage()))
                .when(groupService, verifyIfGroupMemberIsUnique, anyLong(), anyLong());
        groupService.addMember(memberOperationDto);
    }

    @Test
    public void addWritePermission() throws Exception {
        doNothing().when(groupService, verifyGroupPermission, anyString());
        doNothing().when(permissionManagerService).addPermission(any(PermissionDto.class), any(Principal.class), any(Class.class));
        doReturn(groupDto).when(groupService).getById(anyLong());
        doNothing().when(emailSender).sendEmail(anyString(), anyString(), anyString());
        groupService.addWritePermission(groupPermissionDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void addWritePermissionIsNotOwner() throws Exception {
        doThrow(new PermissionException(ErrorMessage.GROUP_ACCESS.getMessage())).when(groupService, verifyGroupPermission, anyString());
        groupService.addWritePermission(groupPermissionDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void addWritePermissionAccessDenied() throws Exception {
        doNothing().when(groupService, verifyGroupPermission, anyString());
        doThrow(new PermissionException(ErrorMessage.ACCESS_DENIED.getMessage()))
                .when(permissionManagerService).addPermission(any(PermissionDto.class), any(Principal.class), any(Class.class));
        groupService.addWritePermission(groupPermissionDto, principal);
    }

    @Test
    public void changeGroupOwnerOk() {
        doNothing().when(permissionManagerService).changeOwner(any(ChangeOwnerDto.class), any(Principal.class), any(Class.class));
        doReturn(groupDto).when(groupService).getById(anyLong());
        doNothing().when(emailSender).sendEmail(anyString(), anyString(), anyString());
        groupService.changeGroupOwner(changeOwnerDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void changeGroupOwnerFail() {
        doThrow(new PermissionException(ErrorMessage.ACCESS_DENIED.getMessage()))
                .when(permissionManagerService).changeOwner(any(ChangeOwnerDto.class), any(Principal.class), any(Class.class));
        groupService.changeGroupOwner(changeOwnerDto, principal);
    }

    @Test
    public void updateOk() throws Exception {
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doNothing().when(groupService, verifyGroupPermission, anyString());
        doNothing().when(groupService, verifyIfGroupNameIsUnique, anyString());
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
        doNothing().when(groupRepository).updateAclSid(anyString(), anyString());
        doReturn(group).when(groupRepository).save(any(Group.class));
        doReturn(groupDto).when(modelMapper).map(any(Group.class), any(Class.class));
        GroupDto actual = groupService.update("group", groupSaveDto);
        assertEquals(actual, groupDto);
    }

    @Test(expected = NotFoundException.class)
    public void updateGroupNotExist() {
        doReturn(Optional.empty()).when(groupRepository).findByName(anyString());
        groupService.update("group", groupSaveDto);
    }

    @Test(expected = PermissionException.class)
    public void updateGroupPermissionFail() throws Exception {
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doThrow(new PermissionException(ErrorMessage.GROUP_ACCESS.getMessage()))
                .when(groupService, verifyGroupPermission, anyString());
        groupService.update("group", groupSaveDto);
    }

    @Test(expected = NotUniqueNameException.class)
    public void updateGroupNotUniqueGroupName() throws Exception {
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doNothing().when(groupService, verifyGroupPermission, anyString());
        doThrow(new NotUniqueNameException(ErrorMessage.GROUP_ALREADY_EXIST.getMessage()))
                .when(groupService, verifyIfGroupNameIsUnique, anyString());
        groupService.update("group", groupSaveDto);
    }

    @Test
    public void deleteCroupOk() throws Exception {
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doNothing().when(groupService, verifyGroupPermission, anyString());
        doNothing().when(groupMemberRepository).deleteByGroupId(anyLong());
        doNothing().when(groupRepository).deleteByName(anyString());
        groupService.deleteCroup("group");
    }

    @Test(expected = NotFoundException.class)
    public void deleteCroupGroupNotExist() {
        doReturn(Optional.empty()).when(groupRepository).findByName(anyString());
        groupService.deleteCroup("group");
    }

    @Test(expected = PermissionException.class)
    public void deleteCroupFailGroupPermission() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doThrow(new PermissionException(ErrorMessage.GROUP_ACCESS.getMessage()))
                .when(permissionManagerService).closeAllPermissions(anyLong(), any(Principal.class), any(Class.class));
        doNothing().when(groupMemberRepository).deleteByGroupId(anyLong());
        doNothing().when(groupRepository).deleteByName(anyString());
        groupService.deleteCroup("group");
    }

    @Test
    public void deleteMemberOk() throws Exception {
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doNothing().when(groupService, verifyGroupPermission, anyString());
        doReturn(Optional.of(user)).when(userRepository).findUserByEmail(anyString());
        doNothing().when(groupMemberRepository).deleteMember(anyLong(), anyLong());
        groupService.deleteMember(memberOperationDto);
    }

    @Test(expected = NotFoundException.class)
    public void deleteMemberGroupNotExist() {
        doReturn(Optional.empty()).when(groupRepository).findByName(anyString());
        groupService.deleteMember(memberOperationDto);
    }

    @Test(expected = PermissionException.class)
    public void deleteMemberGroupPermissionFail() throws Exception {
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doThrow(new PermissionException(ErrorMessage.GROUP_ACCESS.getMessage()))
                .when(groupService, verifyGroupPermission, anyString());
        groupService.deleteMember(memberOperationDto);
    }

    @Test(expected = NotFoundException.class)
    public void deleteMember() throws Exception {
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        doNothing().when(groupService, verifyGroupPermission, anyString());
        doReturn(Optional.empty()).when(userRepository).findUserByEmail(anyString());
        groupService.deleteMember(memberOperationDto);
    }

    @Test
    public void findPrincipalWithAccessToGroupOk() {
        doReturn(Collections.emptyList()).when(permissionManagerService).findPrincipalWithAccess(anyLong(), any(Class.class));
        List<PrincipalPermissionDto> expected = Collections.emptyList();
        Page<PrincipalPermissionDto> actual = groupService.findPrincipalWithAccessToGroup(anyLong(), anyInt(), anyInt());
        assertEquals(actual.getContent(), expected);
    }

    @Test(expected = PermissionException.class)
    public void findPrincipalWithAccessToGroupPrincipalNotFound() {
        doThrow(new PermissionException("")).when(permissionManagerService).findPrincipalWithAccess(anyLong(), any(Class.class));
        groupService.findPrincipalWithAccessToGroup(1L, 1, 1);
    }

    @Test
    public void closePermissionForCertainUserOk() {
        doNothing().when(permissionManagerService).closePermissionForCertainUser(any(PermissionDto.class), any(Principal.class), any(Class.class));
        doReturn(groupDto).when(groupService).getById(anyLong());
        doNothing().when(emailSender).sendEmail(anyString(), anyString(), anyString());
        groupService.closePermissionForCertainUser(groupPermissionDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void closePermissionForCertainUserAccessDenied() {
        doThrow(new PermissionException("")).when(permissionManagerService).closePermissionForCertainUser(any(PermissionDto.class), any(Principal.class), any(Class.class));
        groupService.closePermissionForCertainUser(groupPermissionDto, principal);
    }

    @Test(expected = PermissionException.class)
    public void closePermissionForCertainUserPermissionNotFound() {
        doThrow(new PermissionException("")).when(permissionManagerService).closePermissionForCertainUser(any(PermissionDto.class), any(Principal.class), any(Class.class));
        groupService.closePermissionForCertainUser(groupPermissionDto, principal);
    }

    @Test
    public void verifyIfGroupNameIsUniqueOk() throws Exception {
        doReturn(Optional.empty()).when(groupRepository).findByName(anyString());
        Whitebox.invokeMethod(groupService, verifyIfGroupNameIsUnique, "name");
    }

    @Test(expected = NotUniqueNameException.class)
    public void verifyIfGroupNameIsUniqueFail() throws Exception {
        doReturn(Optional.of(group)).when(groupRepository).findByName(anyString());
        Whitebox.invokeMethod(groupService, verifyIfGroupNameIsUnique, "name");
    }

    @Test
    public void verifyIfGroupMemberIsUniqueOk() throws Exception {
        doReturn(Optional.empty()).when(groupMemberRepository).findOne(anyLong(), anyLong());
        Whitebox.invokeMethod(groupService, verifyIfGroupMemberIsUnique, 1L, 1L);
    }

    @Test(expected = NotUniqueMemberException.class)
    public void verifyIfGroupMemberIsUniqueFail() throws Exception {
        doReturn(Optional.of(groupsMember)).when(groupMemberRepository).findOne(anyLong(), anyLong());
        Whitebox.invokeMethod(groupService, verifyIfGroupMemberIsUnique, 1L, 1L);
    }

    @Test
    public void verifyGroupPermissionOk() throws Exception {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        doReturn("name").when(authentication).getName();
        when(groupRepository.getPermission(anyString(), anyString())).thenReturn(2);
        Whitebox.invokeMethod(groupService, verifyGroupPermission, "id");
    }

    @Test(expected = PermissionException.class)
    public void verifyGroupPermissionFail() throws Exception {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        doReturn("name").when(authentication).getName();
        when(groupRepository.getPermission(anyString(), anyString())).thenReturn(null);
        Whitebox.invokeMethod(groupService, verifyGroupPermission, "id");
    }
}