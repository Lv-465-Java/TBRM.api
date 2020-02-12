package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.resource.ResourceDTO;
import com.softserve.rms.entities.Resource;
import com.softserve.rms.service.ResourceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Override
    public ResourceDTO save(ResourceDTO resourceDTO) {
        return null;
    }

    @Override
    public Resource findById(Long id) {
        return null;
    }

    @Override
    public List<Resource> getAll() {
        return null;
    }

    @Override
    public Resource updateById(Long id, Map<String, Object> body) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
