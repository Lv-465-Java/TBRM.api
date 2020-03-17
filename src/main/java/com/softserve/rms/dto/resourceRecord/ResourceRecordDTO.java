package com.softserve.rms.dto.resourceRecord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRecordDTO {
    private Long id;
    private String name;
    private String description;
    private Long userId;
    private String photos;
    private String document;
    private Map<String, Object> parameters;
}