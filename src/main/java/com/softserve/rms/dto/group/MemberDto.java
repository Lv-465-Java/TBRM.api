package com.softserve.rms.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String email;
    private String firstName;
    private String lastName;
    private String groupName;
}
