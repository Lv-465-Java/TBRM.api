package com.softserve.rms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceParameterDTO {
    private Long id;
    private String name;
    private String typeName;
    private String fieldType;
    private String pattern;
    private Long resourceTemplateId;
}
