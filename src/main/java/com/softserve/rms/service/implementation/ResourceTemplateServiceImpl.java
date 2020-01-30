package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.Person;
import com.softserve.rms.exceptions.NoSuchEntityException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateIsPublishedException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateParameterListIsEmpty;
import com.softserve.rms.repository.PersonRepository;
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
    private final PersonRepository personRepository;
    private ModelMapper modelMapper = new ModelMapper();

    /**
     * Constructor with parameters.
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public ResourceTemplateServiceImpl(ResourceTemplateRepository resourceTemplateRepository,
                                       PersonRepository personRepository) {
        this.resourceTemplateRepository = resourceTemplateRepository;
        this.personRepository = personRepository;
    }

    /**
     * Method creates {@link ResourceTemplate}.
     *
     * @param resourceTemplateSaveDTO {@link ResourceTemplateDTO}
     * @return new {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO save(ResourceTemplateSaveDTO resourceTemplateSaveDTO) {
        ResourceTemplate resourceTemplate = new ResourceTemplate();
        resourceTemplate.setName(resourceTemplateSaveDTO.getName());
        resourceTemplate.setDescription(resourceTemplateSaveDTO.getName());
        resourceTemplate.setTableName(generateResourceTableName(resourceTemplateSaveDTO.getName()));
        resourceTemplate.setPerson(modelMapper.map(personRepository.getOne(resourceTemplateSaveDTO.getPersonId()),
                Person.class));
        resourceTemplate.setIsPublished(false);
        resourceTemplateRepository.save(resourceTemplate);
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
    public ResourceTemplateDTO updateById(Long id, ResourceTemplateSaveDTO resourceTemplateSaveDTO)
            throws NoSuchEntityException {
        ResourceTemplate resourceTemplate = findById(id);
        resourceTemplate.setName(resourceTemplateSaveDTO.getName());
        resourceTemplate.setTableName(generateResourceTableName(resourceTemplateSaveDTO.getName()));
        resourceTemplate.setDescription(resourceTemplateSaveDTO.getDescription());
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
     * Method finds {@link ResourceTemplate} by provided id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplate}
     * @throws NoSuchEntityException if the resource template with provided id is not found
     * @author Halyna Yatseniuk
     */
    public ResourceTemplate findById(Long id) throws NoSuchEntityException {
        return resourceTemplateRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityException(ErrorMessage.CAN_NOT_FIND_A_RESOURCE_TEMPLATE.getMessage()));
    }

    /**
     * Method makes {@link ResourceTemplate} be published.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return boolean value of {@link ResourceTemplateDTO} isPublished field
     * @throws ResourceTemplateIsPublishedException if resource template has been published already
     * @throws ResourceTemplateParameterListIsEmpty if resource template do not have attached parameters
     * @author Halyna Yatseniuk
     */
    @Override
    public Boolean publishResourceTemplate(Long id)
            throws ResourceTemplateIsPublishedException, ResourceTemplateParameterListIsEmpty {
        ResourceTemplate resourceTemplate = findById(id);
        if (verifyIfResourceTemplateIsNotPublished(resourceTemplate) && verifyIfResourceTemplateHasParameters(resourceTemplate)) {
            resourceTemplate.setIsPublished(true);
            resourceTemplateRepository.save(resourceTemplate);
        }
        //create new table method;
        return findById(id).getIsPublished();
    }

    /**
     * Method verifies if {@link ResourceTemplate} is not published.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @return boolean true if {@link ResourceTemplateDTO} is not published
     * @throws ResourceTemplateIsPublishedException if resource template has been already published
     * @author Halyna Yatseniuk
     */
    private Boolean verifyIfResourceTemplateIsNotPublished(ResourceTemplate resourceTemplate)
            throws ResourceTemplateIsPublishedException {
        if (resourceTemplate.getIsPublished()) {
            throw new ResourceTemplateIsPublishedException(ErrorMessage.RESOURCE_TEMPLATE_IS_ALREADY_PUBLISH.getMessage());
        }
        return true;
    }

    /**
     * Method verifies if {@link ResourceTemplate} has attached parameters.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @return boolean true if {@link ResourceTemplateDTO} has attached parameters
     * @throws ResourceTemplateParameterListIsEmpty if resource template do not have attached parameters
     * @author Halyna Yatseniuk
     */
    private Boolean verifyIfResourceTemplateHasParameters(ResourceTemplate resourceTemplate)
            throws ResourceTemplateParameterListIsEmpty {
        if (resourceTemplate.getResourceParameters().isEmpty()) {
            throw new ResourceTemplateParameterListIsEmpty
                    (ErrorMessage.RESOURCE_TEMPLATE_DO_NOT_HAVE_ANY_PARAMETERS.getMessage());
        }
        return true;
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