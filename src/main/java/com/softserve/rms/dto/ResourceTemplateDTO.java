package com.softserve.rms.dto;

import com.softserve.rms.entities.ResourceParameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceTemplateDTO {
    private Long id;
    private String name;
    private String tableName;
    private String description;
    private Long personId;
    private List<ResourceParameterDTO> resourceParameters;
}