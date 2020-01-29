package com.softserve.rms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRelationDTO {
    private Long id;
    private Long relatedResourceTemplateId;
    private Long resourceParameterId;
}
