package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.resource.ResourceDTO;
import com.softserve.rms.dto.resource.ResourceSaveDTO;
import com.softserve.rms.entities.Resource;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.ResourceRepository;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.ResourceService;
import com.softserve.rms.service.ResourceTemplateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ResourceService}
 *
 * @author Andrii Bren
 */
@Service
public class ResourceServiceImpl implements ResourceService {
    private ResourceRepository resourceRepository;
    private ResourceTemplateService resourceTemplateService;
    private UserRepository userRepository;
    private ModelMapper modelMapper = new ModelMapper();

    /**
     * Constructor with parameters
     *
     * @author Andrii Bren
     */
    @Autowired
    public ResourceServiceImpl(ResourceRepository resourceRepository, ResourceTemplateService resourceTemplateService, UserRepository userRepository) {
        this.resourceRepository = resourceRepository;
        this.resourceTemplateService = resourceTemplateService;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public void save(String tableName, ResourceSaveDTO resourceDTO) throws NotFoundException {
        resourceTemplateService.findByName(tableName);
        Resource resource = new Resource();
        resource.setName(resourceDTO.getName());
        resource.setDescription(resourceDTO.getDescription());
        resource.setResourceTemplate(resourceTemplateService.findEntityById(resourceDTO.getResourceTemplateId()));
//        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getOne(resourceDTO.getUserId());
        resource.setUser(user);
        resource.setParameters(resourceDTO.getParameters());
        resourceRepository.save(tableName, resource);
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResourceDTO findByIdDTO(String tableName, Long id) throws NotFoundException {
        return modelMapper.map(findById(tableName, id), ResourceDTO.class);
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    public Resource findById(String tableName, Long id) throws NotFoundException {
        resourceTemplateService.findByName(tableName);
        return resourceRepository.findById(tableName, id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorMessage.CAN_NOT_FIND_A_RESOURCE.getMessage() + id));
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public List<ResourceDTO> findAll(String tableName) throws NotFoundException {
        resourceTemplateService.findByName(tableName);
        List<Resource> resources = resourceRepository.findAll(tableName);
        return resources.stream()
                .map(resource -> modelMapper.map(resource, ResourceDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public void update(String tableName, Long id, Map<String, Object> body)
            throws NotFoundException {
        resourceTemplateService.findByName(tableName);
        Resource resource = findById(tableName, id);
        if (body.get("name") != null) {
            resource.setName(body.get("name").toString());
        }
        if (body.get("description") != null) {
            resource.setDescription(body.get("description").toString());
        }
        if (body.get("parameters") != null) {
            resource.setParameters((HashMap<String, Object>) body.get("parameters"));
        }
        resourceRepository.update(tableName, id, resource);
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public void delete(String tableName, Long id) throws NotFoundException {
        resourceTemplateService.findByName(tableName);
        resourceRepository.delete(tableName, id);
    }
}
