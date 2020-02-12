package com.softserve.rms.service;

import com.softserve.rms.dto.resource.ResourceDTO;
import com.softserve.rms.entities.Resource;

import java.util.List;
import java.util.Map;

public interface ResourceService {

    ResourceDTO save(ResourceDTO resource);

    Resource findById(Long id);

    List<Resource> getAll();

    Resource updateById(Long id, Map<String, Object> body);

    void deleteById(Long id);
}