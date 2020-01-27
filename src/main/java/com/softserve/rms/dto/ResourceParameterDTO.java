package com.softserve.rms.dto;

import com.softserve.rms.entities.ResourceParameter;
import lombok.*;

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

    public ResourceParameterDTO(ResourceParameter resourceParameter) {
        id = resourceParameter.getId();
        name = resourceParameter.getName();
        typeName = resourceParameter.getTypeName();
        fieldType = resourceParameter.getFieldType();
        pattern = resourceParameter.getPattern();
        resourceTemplateId = resourceParameter.getResourceTemplate().getId();
    }
}
