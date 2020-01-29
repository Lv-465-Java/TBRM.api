package com.softserve.rms.dto;

import com.softserve.rms.entities.ParameterType;
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
    private List<ResourceRelationDTO> resourceRelations;

}
