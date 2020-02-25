package com.softserve.rms.dto.resourcerecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRecordSaveDTO {
    private String name;
    private String description;
    private Long userId;
    private Map<String, Object> parameters;
}
