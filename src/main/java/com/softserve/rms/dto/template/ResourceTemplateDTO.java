package com.softserve.rms.dto.template;

import com.softserve.rms.dto.resourceParameter.ResourceParameterDTO;
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
    private Boolean isPublished;
    private String userFirstName;
    private String userLastName;
    private Long userId;
    private List<ResourceParameterDTO> resourceParameters;
}
