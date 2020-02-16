package com.softserve.rms.dto.group;

import com.softserve.rms.dto.UserDto;
import com.softserve.rms.entities.User;
import lombok.Data;

import java.util.List;

@Data
public class GroupDto {
    private String name;
    private String description;
    private List<User> members;
}