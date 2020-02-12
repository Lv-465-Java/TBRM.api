package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateIsPublishedException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateParameterListIsEmpty;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.PermissionManagerService;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.util.Validator;
import org.jooq.DSLContext;
import org.jooq.impl.SQLDataType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
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
    private final UserRepository userRepository;
    private Validator validator = new Validator();
    private ModelMapper modelMapper = new ModelMapper();
    private PermissionManagerService permissionManagerService;
    private DSLContext dslContext;

    /**
     * Constructor with parameters.
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public ResourceTemplateServiceImpl(ResourceTemplateRepository resourceTemplateRepository,
                                       UserRepository userRepository,
                                       PermissionManagerService permissionManagerService, DSLContext dslContext) {
        this.resourceTemplateRepository = resourceTemplateRepository;
        this.userRepository = userRepository;
        this.permissionManagerService = permissionManagerService;
        this.dslContext = dslContext;
    }

    /**
     * Method creates {@link ResourceTemplate}.
     *
     * @param resourceTemplateSaveDTO {@link ResourceTemplateDTO}
     * @return new {@link ResourceTemplateDTO}
     * @throws NotUniqueNameException if the resource template name is not unique
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO save(ResourceTemplateSaveDTO resourceTemplateSaveDTO)
            throws NotUniqueNameException {
        ResourceTemplate resourceTemplate = new ResourceTemplate();
        resourceTemplate.setName(verifyIfResourceTemplateNameIsUnique(resourceTemplateSaveDTO.getName()));
        resourceTemplate.setDescription(resourceTemplateSaveDTO.getDescription());
        resourceTemplate.setTableName(validator.generateTableOrColumnName(resourceTemplateSaveDTO.getName()));
        resourceTemplate.setUser(userRepository.getOne(resourceTemplateSaveDTO.getUserId()));
        resourceTemplate.setIsPublished(false);
        Long resTempId = resourceTemplateRepository.saveAndFlush(resourceTemplate).getId();
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication();
        permissionManagerService.addPermissionForResourceTemplate(new PermissionDto(resTempId, principal.getName(), "write", true), principal);
        permissionManagerService.addPermissionForResourceTemplate(new PermissionDto(resTempId, "ROLE_MANAGER", "read", false), principal);
        return modelMapper.map(resourceTemplate, ResourceTemplateDTO.class);
    }

    /**
     * Method finds {@link ResourceTemplate} by provided id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @throws NotFoundException if the resource template is not found
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO findDTOById(Long id) throws NotFoundException {
        return modelMapper.map(findEntityById(id), ResourceTemplateDTO.class);
    }

    /**
     * Method finds all {@link ResourceTemplate}.
     *
     * @return list of all {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @Override
    public List<ResourceTemplateDTO> getAll() {
        List<ResourceTemplate> resourceTemplates = resourceTemplateRepository.findAll();
        return resourceTemplates.stream()
                .map(resourceTemplate -> modelMapper.map(resourceTemplate, ResourceTemplateDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Method finds all {@link ResourceTemplate} created by provided person id.
     *
     * @param id of {@link User}
     * @return list of {@link ResourceTemplateDTO} with appropriate person id
     * @author Halyna Yatseniuk
     */
    @Override
    public List<ResourceTemplateDTO> getAllByUserId(Long id) {
        List<ResourceTemplate> resourceTemplates = resourceTemplateRepository.findAllByUserId(id);
        return resourceTemplates.stream()
                .map(resourceTemplate -> modelMapper.map(resourceTemplate, ResourceTemplateDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Method updates {@link ResourceTemplate} by id.
     *
     * @param id   of {@link ResourceTemplateDTO}
     * @param body map containing String key and Object value
     * @return {@link ResourceTemplateDTO}
     * @throws NotFoundException      if the resource template is not found
     * @throws NotUniqueNameException if the resource template name is not unique
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO updateById(Long id, Map<String, Object> body)
            throws NotFoundException, NotUniqueNameException {
        ResourceTemplate resourceTemplate = findEntityById(id);
        if (body.get("name") != null) {
            resourceTemplate.setName(verifyIfResourceTemplateNameIsUnique(body.get("name").toString()));
            resourceTemplate.setTableName(validator.generateTableOrColumnName(body.get("name").toString()));
        }
        if (body.get("description") != null) {
            resourceTemplate.setDescription(body.get("description").toString());
        }
        resourceTemplateRepository.save(resourceTemplate);
        return modelMapper.map(resourceTemplate, ResourceTemplateDTO.class);
    }

    /**
     * Method deletes {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @throws NotFoundException if the resource template with provided id is not found
     * @author Halyna Yatseniuk
     */
    @Override
    @Transactional
    public void deleteById(Long id) throws NotFoundException {
        try {
            resourceTemplateRepository.deleteById(id);
            Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication();
            permissionManagerService.closeAllPermissionsToResource(id, principal);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(ErrorMessage.CAN_NOT_FIND_A_RESOURCE_TEMPLATE.getMessage());
        }
    }

    /**
     * Method finds all {@link ResourceTemplate} by name or description.
     *
     * @param searchedWord request parameter to search resource templates
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @Override
    public List<ResourceTemplateDTO> searchByNameOrDescriptionContaining(String searchedWord) {
        List<ResourceTemplate> resourceTemplates = resourceTemplateRepository.
                findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(searchedWord, searchedWord);
        return resourceTemplates.stream()
                .map(resourceTemplate -> modelMapper.map(resourceTemplate, ResourceTemplateDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Method finds {@link ResourceTemplate} by provided id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplate}
     * @throws NotFoundException if the resource template with provided id is not found
     * @author Halyna Yatseniuk
     */
    public ResourceTemplate findEntityById(Long id) throws NotFoundException {
        return resourceTemplateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.CAN_NOT_FIND_A_RESOURCE_TEMPLATE.getMessage()));
    }


//    /**
//     * Method makes {@link ResourceTemplate} be published.
//     *
//     * @param id of {@link ResourceTemplateDTO}
//     * @return boolean value of {@link ResourceTemplateDTO} isPublished field
//     * @throws ResourceTemplateIsPublishedException if resource template has been published already
//     * @throws ResourceTemplateParameterListIsEmpty if resource template do not have attached parameters
//     * @author Halyna Yatseniuk
//     */

    public void checkSomething(Long id, Map<String, Object> body) {
        ResourceTemplate resourceTemplate = findEntityById(id);
        if (body.get("isPublished").equals(true)) {
            publishResourceTemplate(resourceTemplate);
        } else if (body.get("isPublished").equals(false)) {
            unPublishResourceTemplate(resourceTemplate);
        }
    }


    @Override
    public Boolean publishResourceTemplate(ResourceTemplate resourceTemplate)
            throws ResourceTemplateIsPublishedException, ResourceTemplateParameterListIsEmpty {
        if (verifyIfResourceTemplateIsNotPublished(resourceTemplate) && verifyIfResourceTemplateHasParameters(resourceTemplate)) {
            resourceTemplate.setIsPublished(true);
            resourceTemplateRepository.save(resourceTemplate);
            createResourceTable(resourceTemplate);
            //create table
        }
        return findEntityById(resourceTemplate.getId()).getIsPublished();
    }

    private void createResourceTable(ResourceTemplate resourceTemplate) {
        dslContext.createTable(resourceTemplate.getTableName())
                .column("Id", SQLDataType.BIGINT)
                .column("Name", SQLDataType.VARCHAR(255))
                .column("Description", SQLDataType.VARCHAR(255))
                .execute();
    }

    /**
     * Method cancels {@link ResourceTemplate} publish.
     * <p>
     * //     * @param id of {@link ResourceTemplateDTO}
     *
     * @return boolean value of {@link ResourceTemplateDTO} isPublished field
     * @author Halyna Yatseniuk
     */
    public Boolean unPublishResourceTemplate(ResourceTemplate resourceTemplate) {
        //verifications
        resourceTemplate.setIsPublished(false);
        resourceTemplateRepository.save(resourceTemplate);
        //drop table
        return !findEntityById(resourceTemplate.getId()).getIsPublished();
    }

    /**
     * Method verifies if {@link ResourceTemplate} name is unique.
     *
     * @param name of {@link ResourceTemplateDTO}
     * @return string of {@link ResourceTemplateDTO} name if it is unique
     * @throws NotUniqueNameException if the resource template name is not unique
     * @author Halyna Yatseniuk
     */
    private String verifyIfResourceTemplateNameIsUnique(String name) throws NotUniqueNameException {
        if (resourceTemplateRepository.findByName(name).isPresent()) {
            throw new NotUniqueNameException(ErrorMessage.RESOURCE_TEMPLATE_NAME_IS_NOT_UNIQUE.getMessage());
        }
        return name;
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
