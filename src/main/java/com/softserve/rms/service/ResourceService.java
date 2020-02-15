package com.softserve.rms.service;

import com.softserve.rms.dto.resource.ResourceDTO;
import com.softserve.rms.dto.resource.ResourceSaveDTO;
import com.softserve.rms.entities.Resource;

import java.util.List;
import java.util.Map;

public interface ResourceService {

    ResourceSaveDTO save(ResourceSaveDTO resource);

    ResourceDTO findById(String resourceName, Long id);

    List<ResourceDTO> findAll(String name);

    ResourceDTO updateById(Long id, Map<String, Object> body);

    void deleteById(Long id);
}
