package com.softserve.rms.dto.group;

import lombok.Data;

import java.util.List;

@Data
public class GroupDto {
    private String name;
    private String description;
    private List<MemberDto> members;
}
