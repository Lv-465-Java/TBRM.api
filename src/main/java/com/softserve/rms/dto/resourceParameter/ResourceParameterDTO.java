package com.softserve.rms.dto.resourceParameter;

import com.softserve.rms.entities.ParameterType;
import lombok.*;

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
    private String relatedResourceTemplateName;
    private Long relatedResourceTemplateId;
//    private ResourceRelationDTO resourceRelation;
}
