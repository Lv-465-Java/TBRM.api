package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.Person;
import com.softserve.rms.exceptions.resourseTemplate.NameIsNotUniqueException;
import com.softserve.rms.exceptions.resourseTemplate.NoSuchResourceTemplateException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateIsPublishedException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateParameterListIsEmpty;
import com.softserve.rms.repository.PersonRepository;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.validator.ResourceTemplateAndParameterValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ResourceTemplateAndParameterValidator validator = new ResourceTemplateAndParameterValidator();
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
        resourceTemplate.setName(verifyIfResourceTemplateNameIsUnique(resourceTemplateSaveDTO.getName()));
        resourceTemplate.setDescription(resourceTemplateSaveDTO.getName());
        resourceTemplate.setTableName(validator.generateTableOrColumnName(resourceTemplateSaveDTO.getName()));
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
     * @throws NoSuchResourceTemplateException if the resource template is not found
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO findDTOById(Long id) throws NoSuchResourceTemplateException {
        return modelMapper.map(findEntityById(id), ResourceTemplateDTO.class);
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
     * @throws NoSuchResourceTemplateException if the resource template is not found
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO updateById(Long id, ResourceTemplateSaveDTO resourceTemplateSaveDTO)
            throws NoSuchResourceTemplateException {
        ResourceTemplate resourceTemplate = findEntityById(id);
        resourceTemplate.setName(resourceTemplateSaveDTO.getName());
        resourceTemplate.setTableName(validator.generateTableOrColumnName(resourceTemplateSaveDTO.getName()));
        resourceTemplate.setDescription(resourceTemplateSaveDTO.getDescription());
        resourceTemplateRepository.save(resourceTemplate);
        return modelMapper.map(resourceTemplate, ResourceTemplateDTO.class);
    }

    /**
     * Method deletes {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @throws NoSuchResourceTemplateException if the resource template with provided id is not found
     * @author Halyna Yatseniuk
     */
    @Override
    @Transactional
    public void deleteById(Long id) throws NoSuchResourceTemplateException {
        try {
            resourceTemplateRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NoSuchResourceTemplateException(ErrorMessage.CAN_NOT_FIND_A_RESOURCE_TEMPLATE.getMessage());
        }
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
                .findByTableNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(name, description.toLowerCase());
        return resourceTemplates.stream()
                .map(resourceTemplate -> modelMapper.map(resourceTemplate, ResourceTemplateDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Method finds {@link ResourceTemplate} by provided id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplate}
     * @throws NoSuchResourceTemplateException if the resource template with provided id is not found
     * @author Halyna Yatseniuk
     */
    public ResourceTemplate findEntityById(Long id) throws NoSuchResourceTemplateException {
        return resourceTemplateRepository.findById(id)
                .orElseThrow(() -> new NoSuchResourceTemplateException(ErrorMessage.CAN_NOT_FIND_A_RESOURCE_TEMPLATE.getMessage()));
    }

    /**
     * Method verifies if {@link ResourceTemplate} name is unique.
     *
     * @param name of {@link ResourceTemplateDTO}
     * @return string of {@link ResourceTemplateDTO} name if it's unique
     * @throws NameIsNotUniqueException if the resource template name is not unique
     * @author Halyna Yatseniuk
     */
    public String verifyIfResourceTemplateNameIsUnique(String name) throws NameIsNotUniqueException {
        if (resourceTemplateRepository.findByName(name).isPresent()) {
            throw new NameIsNotUniqueException(ErrorMessage.RESOURCE_TEMPLATE_NAME_IS_NOT_UNIQUE.getMessage());
        }
        return name;
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
        ResourceTemplate resourceTemplate = findEntityById(id);
        if (verifyIfResourceTemplateIsNotPublished(resourceTemplate) && verifyIfResourceTemplateHasParameters(resourceTemplate)) {
            resourceTemplate.setIsPublished(true);
            resourceTemplateRepository.save(resourceTemplate);
        }
        //create new table method;
        return findEntityById(id).getIsPublished();
    }

    public Boolean unPublishResourceTemplate(Long id) {
        ResourceTemplate resourceTemplate = findEntityById(id);
        resourceTemplate.setIsPublished(false);
        resourceTemplateRepository.save(resourceTemplate);
        //TODO
        //Verify if table has at least one resource entity
        return findEntityById(id).getIsPublished();
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
            throw new ResourceTemplateIsPublishedException
                    (ErrorMessage.RESOURCE_TEMPLATE_IS_ALREADY_PUBLISHED.getMessage());
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
}