package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.ResourceTemplateDTO;
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
    public ResourceTemplateDTO save(ResourceTemplateDTO resourceTemplateDTO) {
        resourceTemplateDTO.setTableName(generateResourceTableName(resourceTemplateDTO.getName()));
        ResourceTemplate resourceTemplate = resourceTemplateRepository
                .save(modelMapper.map(resourceTemplateDTO, ResourceTemplate.class));
        return modelMapper.map(resourceTemplate, ResourceTemplateDTO.class);
    }

    /**
     * Method finds {@link ResourceTemplate} by provided id.
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
     * Method finds all {@link ResourceTemplate} created by provided person id.
     *
     * @param id of {@link Person}
     * @return list of {@link ResourceTemplateDTO} with appropriate person id
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
        resourceTemplate.setName(resourceTemplateDTO.getName());
        resourceTemplate.setTableName(generateResourceTableName(resourceTemplateDTO.getName()));
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
    public boolean deleteById(Long id) {
        resourceTemplateRepository.deleteById(id);
        return !resourceTemplateRepository.findById(id).isPresent();
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

    /**
     * Method finds  {@link ResourceTemplate} by provided id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplate}
     * @throws NoSuchEntityException if the resource template with provided id is not found
     * @author Halyna Yatseniuk
     */
    private ResourceTemplate findById(Long id) throws NoSuchEntityException {
        return resourceTemplateRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException(ErrorMessage.CAN_NOT_FIND_A_RESOURCE_TEMPLATE.getMessage()));
    }

    /**
     * Method generates String for {@link ResourceTemplate} table name field.
     *
     * @param name of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    private String generateResourceTableName(String name) {
        return name.toLowerCase().replaceAll("[-!$%^&*()_+|~=`\\[\\]{}:\";'<>?,. ]", "_");
    }
}