package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.resource.ResourceDTO;
import com.softserve.rms.dto.resource.ResourceSaveDTO;
import com.softserve.rms.entities.Resource;
import com.softserve.rms.entities.User;
import com.softserve.rms.repository.ResourceRepository;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.ResourceService;
import com.softserve.rms.service.ResourceTemplateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    private ResourceRepository resourceRepository;
    private ResourceTemplateService resourceTemplateService;
    private UserRepository userRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public ResourceServiceImpl(ResourceRepository resourceRepository, ResourceTemplateService resourceTemplateService, UserRepository userRepository) {
        this.resourceRepository = resourceRepository;
        this.resourceTemplateService = resourceTemplateService;
        this.userRepository = userRepository;
    }

    @Override
    public ResourceSaveDTO save(ResourceSaveDTO resourceDTO) {
        Resource resource = new Resource();
        resource.setName(resourceDTO.getName());
        resource.setDescription(resourceDTO.getDescription());
        resource.setResourceTemplate(resourceTemplateService.findEntityById(resourceDTO.getResourceTemplateId()));
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findUserByEmail(principal.getName()).get();
        resource.setUser(user);
        resource.setParameters(resourceDTO.getParameters());
//        resource.setResourceParameters(resourceDTO.getResourceParameters());
        resourceRepository.save(resource);
        return new ResourceSaveDTO();
    }

    @Override
    public ResourceDTO findById(String resourceName, Long id) {
        return modelMapper.map(resourceRepository.findById(resourceName, id), ResourceDTO.class);
    }

    @Override
    public List<ResourceDTO> findAll(String name) {
        List<Resource> resources = resourceRepository.findAll(name);
        return resources.stream()
                .map(resource -> modelMapper.map(resource, ResourceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Resource updateById(Long id, Map<String, Object> body) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
