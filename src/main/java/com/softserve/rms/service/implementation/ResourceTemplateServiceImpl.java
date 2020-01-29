package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.Person;
import com.softserve.rms.exceptions.NoSuchEntityException;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.service.ResourceTemplateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ResourceTemplateService}.
 *
 * @author Halyna Yatseniuk
 */
@Service
public class ResourceTemplateServiceImpl implements ResourceTemplateService {
    private final ResourceTemplateRepository resourceTemplateRepository;
    private ModelMapper modelMapper = new ModelMapper();

    /**
     * Constructor with parameters.
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public ResourceTemplateServiceImpl(ResourceTemplateRepository resourceTemplateRepository) {
        this.resourceTemplateRepository = resourceTemplateRepository;
    }

    /**
     * Method creates {@link ResourceTemplate}.
     *
     * @param resourceTemplateDTO {@link ResourceTemplateDTO}
     * @return new {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO create(ResourceTemplateDTO resourceTemplateDTO) {
        ResourceTemplate resourceTemplate = resourceTemplateRepository
                .save(modelMapper.map(resourceTemplateDTO, ResourceTemplate.class));
        return modelMapper.map(resourceTemplate, ResourceTemplateDTO.class);
    }

    /**
     * Method finds {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @throws NoSuchEntityException if the resource template is not found
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO getById(Long id) throws NoSuchEntityException {
        return modelMapper.map(findById(id), ResourceTemplateDTO.class);
    }

    /**
     * Method finds all {@link ResourceTemplate} by user id.
     *
     * @param id of {@link Person}
     * @return List of all {@link ResourceTemplateDTO} for user
     * @author Halyna Yatseniuk
     */
    @Override
    public List<ResourceTemplateDTO> getAllByPersonId(Long id) {
        List<ResourceTemplate> resourceTemplates = resourceTemplateRepository.findAllByPersonId(id);
        return resourceTemplates.stream()
                .map(resourceTemplate -> modelMapper.map(resourceTemplate, ResourceTemplateDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Method updates {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @throws NoSuchEntityException if the resource template is not found
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO updateById(Long id, ResourceTemplateDTO resourceTemplateDTO) throws NoSuchEntityException {
        ResourceTemplate resourceTemplate = findById(id);
        resourceTemplate.setTableName(resourceTemplateDTO.getTableName());
        resourceTemplate.setDescription(resourceTemplateDTO.getDescription());
        resourceTemplateRepository.save(resourceTemplate);
        return modelMapper.map(resourceTemplate, ResourceTemplateDTO.class);
    }

    /**
     * Method deletes {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @Override
    public void deleteById(Long id) {
        resourceTemplateRepository.deleteById(id);
    }

    /**
     * Method finds all {@link ResourceTemplate} by name or description.
     *
     * @param body map containing String key and String value
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @Override
    public List<ResourceTemplateDTO> searchByNameOrDescriptionContaining(Map<String, String> body) {
        String name = body.get("name");
        String description = body.get("description");
        List<ResourceTemplate> resourceTemplates = resourceTemplateRepository
                .findByTableNameContainingOrDescriptionContaining(name, description);
        return resourceTemplates.stream()
                .map(resourceTemplate -> modelMapper.map(resourceTemplate, ResourceTemplateDTO.class))
                .collect(Collectors.toList());
    }

    private ResourceTemplate findById(Long id) {
        return resourceTemplateRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException(ErrorMessage.CAN_NOT_FIND_A_RESOURCE_TEMPLATE.getMessage()));
    }

//    public ResourceTemplate getById(Long id) {
//        return resourceTemplateRepository.findById(id)
//                .orElseThrow(()-> new RuntimeException("Error"));
//    }
}