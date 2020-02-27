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
//    private Long resourceTemplateId;
    private Long userId;
    private Map<String, Object> parameters;
}
