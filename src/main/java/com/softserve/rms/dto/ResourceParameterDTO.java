package com.softserve.rms.dto;

import com.softserve.rms.entities.ParameterType;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceRelation;
import com.softserve.rms.entities.ResourceTemplate;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceParameterDTO {
    private Long id;
    private String columnName;
    private ParameterType parameterType;
    private String pattern;
    private Long resourceTemplateId;
//    private List<Long> resourceRelationIds;
    private List<ResourceRelationDTO> resourceRelations;

}
