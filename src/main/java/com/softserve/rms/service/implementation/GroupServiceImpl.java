package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.group.GroupDto;
import com.softserve.rms.dto.group.GroupSaveDto;
import com.softserve.rms.dto.group.MemberDto;
import com.softserve.rms.dto.group.MemberOperationDto;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
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
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public GroupServiceImpl(UserRepository userRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public List<GroupDto> getAll() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(principal.getName()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.USER_DO_NOT_EXISTS.getMessage())
        );
        return groupRepository.findAllByOwnerId(user.getId()).stream()
                .map(group -> modelMapper.map(group, GroupDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public GroupDto getByName(String name) {
        return null;
    }

    @Override
    public GroupDto createGroup(GroupSaveDto groupSaveDto) {
        return null;
    }

    @Override
    public MemberDto addMember(MemberOperationDto memberSaveDto) {
        return null;
    }

    @Override
    public void deleteCroup(String groupName) {

    }

    @Override
    public void deleteMember(MemberOperationDto memberDeleteDto) {

    }
}
