package com.softserve.rms.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private String name;
    private String description;
    private List<MemberDto> users;
}