package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.constants.Message;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.dto.group.*;
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
import com.softserve.rms.util.EmailSender;
import com.softserve.rms.util.PaginationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

import static com.softserve.rms.util.PaginationUtil.validatePage;
import static com.softserve.rms.util.PaginationUtil.validatePageSize;

@Service
public class GroupServiceImpl  implements GroupService {
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private GroupMemberRepository groupMemberRepository;
    private PermissionManagerService permissionManagerService;
    private ModelMapper modelMapper;
    private final String writePermission = "write";
    private final EmailSender emailSender;

    @Autowired
    public GroupServiceImpl(UserRepository userRepository, GroupRepository groupRepository,
                            GroupMemberRepository groupMemberRepository,
                            PermissionManagerService permissionManagerService, ModelMapper modelMapper, EmailSender emailSender) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.permissionManagerService = permissionManagerService;
        this.modelMapper = modelMapper;
        this.emailSender = emailSender;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<GroupDto> getAll(Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(validatePage(page), validatePageSize(pageSize), Sort.Direction.DESC, "id");
        return groupRepository.findAll(pageable)
                .map(group -> modelMapper.map(group, GroupDto.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<MemberDto> getAllMembers(Long groupId, Integer page, Integer pageSize) {
        Pageable pageable = PageRequest.of(validatePage(page), validatePageSize(pageSize));
        Page<User> users = groupRepository.findAllMembers(groupId, pageable);
        return users.map(user -> modelMapper.map(user, MemberDto.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupDto getById(Long id) {
        return modelMapper.map(groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.GROUP_DO_NOT_EXISTS.getMessage())), GroupDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupDto getByName(String name) throws NotFoundException {
        return modelMapper.map(groupRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.GROUP_DO_NOT_EXISTS.getMessage())), GroupDto.class);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public GroupDto createGroup(GroupSaveDto groupSaveDto) throws NotFoundException, NotUniqueNameException {
        if(groupSaveDto.getName() == null) {
            throw new RuntimeException(ErrorMessage.CANNOT_ADD_EMPTY_NAME.getMessage());
        }
        verifyIfGroupNameIsUnique(groupSaveDto.getName());
        Group newGroup = groupRepository.save(modelMapper.map(groupSaveDto, Group.class));
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        String mail = principal.getName();
        User user = userRepository.findUserByEmail(mail).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_DO_NOT_EXISTS.getMessage())
        );
        permissionManagerService.addPermission(new PermissionDto(newGroup.getId(), user.getEmail(), "write", true),
                principal, Group.class);
        return modelMapper.map(newGroup, GroupDto.class);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void addWritePermission(GroupPermissionDto groupPermissionDto, Principal principal) {
        verifyGroupPermission(groupPermissionDto.getId().toString());
        PermissionDto permissionDto = new PermissionDto(groupPermissionDto.getId(), groupPermissionDto.getRecipient(), writePermission, true);
        permissionManagerService.addPermission(permissionDto, principal, Group.class);
        String message = String.format(Message.ACCESS.toString(), principal.getName(), writePermission,
                "group " + getById(groupPermissionDto.getId()).getName(), String.format(Message.LINK.toString(), "groups"));
        emailSender.sendEmail(Message.GROUP_PERMISSION_SUBJECT.toString(), message, groupPermissionDto.getRecipient());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changeGroupOwner(ChangeOwnerDto changeOwnerDto, Principal principal) {
        permissionManagerService.changeOwner(changeOwnerDto, principal, Group.class);
        String message = String.format(Message.OWNER.toString(), principal.getName(), writePermission,
                "group " + getById(changeOwnerDto.getId()).getName(), String.format(Message.LINK.toString(), "groups"));
        emailSender.sendEmail(Message.GROUP_PERMISSION_SUBJECT.toString(), message, changeOwnerDto.getRecipient());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public GroupDto update(String name, GroupSaveDto groupSaveDto) throws NotFoundException, NotUniqueNameException {
        Group group = groupRepository.findByName(name).orElseThrow(
                () -> new NotFoundException(ErrorMessage.GROUP_DO_NOT_EXISTS.getMessage())
        );
        verifyGroupPermission(group.getId().toString());
        verifyIfGroupNameIsUnique(groupSaveDto.getName());
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

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteCroup(String groupName) throws NotFoundException {
        Group group = groupRepository.findByName(groupName).orElseThrow(
                () -> new NotFoundException(ErrorMessage.GROUP_DO_NOT_EXISTS.getMessage())
        );
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        permissionManagerService.closeAllPermissions(group.getId(),principal, Group.class);
        groupMemberRepository.deleteByGroupId(group.getId());
        groupRepository.deleteByName(groupName);
    }

    /**
     * {@inheritDoc}
     */
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
     * {@inheritDoc}
     */
    @Override
    public Page<PrincipalPermissionDto> findPrincipalWithAccessToGroup(Long id, Integer page, Integer pageSize) {
        List<PrincipalPermissionDto> usersWithPermission = permissionManagerService.findPrincipalWithAccess(id, Group.class);
        return PaginationUtil.buildPage(usersWithPermission, page, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closePermissionForCertainUser(GroupPermissionDto groupPermissionDto, Principal principal) {
        PermissionDto permissionDto = new PermissionDto(groupPermissionDto.getId(), groupPermissionDto.getRecipient(), writePermission, true);
        permissionManagerService.closePermissionForCertainUser(permissionDto, principal, Group.class);
        String message = String.format(Message.DENIED.toString(), principal.getName(), writePermission,
                "group " + getById(groupPermissionDto.getId()).getName(), String.format(Message.LINK.toString(), "groups"));
        emailSender.sendEmail(Message.GROUP_PERMISSION_SUBJECT.toString(), message, groupPermissionDto.getRecipient());
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

    /**
     * Methods verifies if {@link User} has
     * write permission on {@link Group}
     *
     * @param groupId group id
     * @throws PermissionException if user has no access to group
     * @author Artur Sydor
     */
    private void verifyGroupPermission(String groupId) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        Integer mask = groupRepository.getPermission(principal.getName(), groupId);
        if (mask == null || mask != BasePermission.WRITE.getMask()) {
            throw new PermissionException(ErrorMessage.GROUP_ACCESS.getMessage());
        }
    }
}