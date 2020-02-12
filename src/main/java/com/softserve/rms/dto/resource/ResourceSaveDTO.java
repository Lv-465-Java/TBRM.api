package com.softserve.rms.dto.resource;

import com.softserve.rms.entities.ResourceParameter;

import java.util.List;

public class ResourceSaveDTO {
    private String name;
    private String description;
    private Long resourceTemplateId;
    private Long userId;
    private List<ResourceParameter> resourceParameters;
}