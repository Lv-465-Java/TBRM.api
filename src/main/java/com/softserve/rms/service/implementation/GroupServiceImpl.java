package com.softserve.rms.service.implementation;

import com.amazonaws.services.codecommit.model.UserInfo;
import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.group.GroupDto;
import com.softserve.rms.dto.group.GroupSaveDto;
import com.softserve.rms.dto.group.MemberDto;
import com.softserve.rms.dto.group.MemberOperationDto;
import com.softserve.rms.entities.Group;
import com.softserve.rms.entities.GroupMember;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.GroupMemberRepository;
import com.softserve.rms.repository.GroupRepository;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.GroupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl  implements GroupService {
    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private GroupMemberRepository groupMemberRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public GroupServiceImpl(UserRepository userRepository, GroupRepository groupRepository,
                            GroupMemberRepository groupMemberRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    @Override
    public List<GroupDto> getAll() {
        return groupRepository.findAll().stream()
                .map(group -> modelMapper.map(group, GroupDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public GroupDto getByName(String name) {
        return modelMapper.map(groupRepository.findByName(name), GroupDto.class);
    }

    @Override
    public GroupDto createGroup(GroupSaveDto groupSaveDto) {
        Group newGroup = groupRepository.save(modelMapper.map(groupSaveDto, Group.class));
        return modelMapper.map(newGroup, GroupDto.class);
    }

    @Override
    public MemberDto addMember(MemberOperationDto memberSaveDto) throws NotFoundException{
        User user = userRepository.findUserByEmail(memberSaveDto.getEmail()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_DO_NOT_EXISTS.getMessage())
        );
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(user);
        Group group = groupRepository.findByName(memberSaveDto.getGroupName());
        System.out.println(group);
        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setUser(user);
        groupMemberRepository.save(groupMember);
        System.out.println(groupMember);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return new MemberDto(user.getEmail(), user.getFirstName(), user.getLastName(), group.getName());
    }

    @Override
    public void update(Group group) {

    }

    @Override
    public void deleteCroup(String groupName) {
        groupRepository.deleteByName(groupName);
    }

    @Override
    public void deleteMember(MemberOperationDto memberDeleteDto) {
        User user = userRepository.findUserByEmail(memberDeleteDto.getEmail()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_DO_NOT_EXISTS.getMessage())
        );
        Group group = groupRepository.findByName(memberDeleteDto.getGroupName());
        GroupMember groupMember = new GroupMember();
        groupMember.setGroup(group);
        groupMember.setUser(user);
        groupMemberRepository.delete(groupMember);
    }
}
