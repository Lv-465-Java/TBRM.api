package com.softserve.rms.dto.resourceparameter;

import com.softserve.rms.entities.ParameterType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceParameterDTO {
    private Long id;
    private String name;
    private String columnName;
    private ParameterType parameterType;
    private String pattern;
    private Long resourceTemplateId;
    private ResourceRelationDTO resourceRelation;
}