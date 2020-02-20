package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.NotDeletedException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.exceptions.resourseTemplate.*;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.repository.implementation.JooqDDL;
import com.softserve.rms.service.PermissionManagerService;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.util.Validator;
import org.jooq.*;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private UserServiceImpl userService;
    private PermissionManagerService permissionManagerService;
    private Validator validator = new Validator();
    private ModelMapper modelMapper = new ModelMapper();
    private DSLContext dslContext;
    private JooqDDL jooqDDL;

    private Logger Log = LoggerFactory.getLogger(ResourceTemplateServiceImpl.class);

    /**
     * Constructor with parameters.
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public ResourceTemplateServiceImpl(ResourceTemplateRepository resourceTemplateRepository,
                                       UserServiceImpl userService, PermissionManagerService permissionManagerService,
                                       DSLContext dslContext) {
        this.resourceTemplateRepository = resourceTemplateRepository;
        this.userService = userService;
        this.permissionManagerService = permissionManagerService;
        this.dslContext = dslContext;
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO save(ResourceTemplateSaveDTO resourceTemplateSaveDTO)
            throws NotUniqueNameException {
        Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication();
        ResourceTemplate resourceTemplate = new ResourceTemplate();
        resourceTemplate.setName(verifyIfResourceTemplateNameIsUnique(resourceTemplateSaveDTO.getName()));
        resourceTemplate.setTableName(verifyIfResourceTemplateTableNameIsUnique(resourceTemplateSaveDTO.getName()));
        resourceTemplate.setDescription(resourceTemplateSaveDTO.getDescription());
        String prName = principal.getName();
        resourceTemplate.setUser(userService.getUserByEmail(principal.getName()));
        resourceTemplate.setIsPublished(false);
        Long resTempId = resourceTemplateRepository.saveAndFlush(resourceTemplate).getId();
        setAccessToTemplate(resTempId, principal);
        return modelMapper.map(resourceTemplate, ResourceTemplateDTO.class);
    }

    // javadoc
    public void setAccessToTemplate(Long resTempId, Principal principal) {
        permissionManagerService.addPermissionForResourceTemplate(
                new PermissionDto(resTempId, principal.getName(), "write", true), principal);
        permissionManagerService.addPermissionForResourceTemplate(
                new PermissionDto(resTempId, "ROLE_MANAGER", "read", false), principal);
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO findDTOById(Long id) throws NotFoundException {
        return modelMapper.map(findEntityById(id), ResourceTemplateDTO.class);
    }

    /**
     * {@inheritDoc}
     *
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
     * {@inheritDoc}
     *
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
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    @Transactional
    public ResourceTemplateDTO checkIfTemplateCanBeUpdated(Long id, Map<String, Object> body)
            throws NotFoundException, NotUniqueNameException, ResourceTemplateCanNotBeModified {
        ResourceTemplate resourceTemplate = findEntityById(id);
        if (resourceTemplate.getIsPublished().equals(false)) {
            return updateById(resourceTemplate, body);
        } else
            throw new ResourceTemplateCanNotBeModified(ErrorMessage.RESOURCE_TEMPLATE_CAN_NOT_BE_UPDATED.getMessage());
    }

    /**
     * Method updates fields of {@link ResourceTemplate}.
     *
     * @param resourceTemplate of {@link ResourceTemplateDTO}
     * @param body             map containing String key and Object value
     * @throws NotUniqueNameException if the resource template name is not unique
     * @author Halyna Yatseniuk
     */
    private ResourceTemplateDTO updateById(ResourceTemplate resourceTemplate, Map<String, Object> body)
            throws NotUniqueNameException {
        if (body.get(FieldConstants.NAME.getValue()) != null) {
            resourceTemplate.setName(verifyIfResourceTemplateNameIsUnique(
                    body.get(FieldConstants.NAME.getValue()).toString()));
            resourceTemplate.setTableName(verifyIfResourceTemplateTableNameIsUnique(
                    body.get(FieldConstants.NAME.getValue()).toString()));
        }
        if (body.get(FieldConstants.DESCRIPTION.getValue()) != null) {
            resourceTemplate.setDescription(body.get(FieldConstants.DESCRIPTION.getValue()).toString());
        }
        resourceTemplateRepository.save(resourceTemplate);
        return modelMapper.map(resourceTemplate, ResourceTemplateDTO.class);
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    @Transactional
    public void checkIfTemplateCanBeDeleted(Long id) throws NotFoundException {
        if (findEntityById(id).getIsPublished().equals(false)) {
            deleteById(id);
        } else throw new ResourceTemplateCanNotBeModified
                (ErrorMessage.RESOURCE_TEMPLATE_CAN_NOT_BE_DELETED.getMessage());
    }

    /**
     * Method deletes {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @throws NotDeletedException if the resource template with provided id is not deleted
     * @author Halyna Yatseniuk
     */
    public void deleteById(Long id) {
        try {
            resourceTemplateRepository.deleteById(id);
            Principal principal = (Principal) SecurityContextHolder.getContext().getAuthentication();
            permissionManagerService.closeAllPermissionsToResource(id, principal);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException(ErrorMessage.CAN_NOT_FIND_A_RESOURCE_TEMPLATE.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
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
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    @Transactional
    public void selectPublishOrCancelPublishAction(Long id, Map<String, Object> body) {
        ResourceTemplate resourceTemplate = findEntityById(id);
        if (body.get(FieldConstants.IS_PUBLISHED.getValue()).equals(true)) {
            publishResourceTemplate(resourceTemplate);
        } else {
            unPublishResourceTemplate(resourceTemplate);
        }
    }

    /**
     * Method finds {@link ResourceTemplate} by name.
     *
     * @param name of {@link ResourceTemplate}
     * @throws NotFoundException if resource template is not found
     * @author Andrii Bren
     */
    @Override
    public ResourceTemplate findByName(String name) {
        return resourceTemplateRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.CAN_NOT_FIND_A_RESOURCE_TABLE.getMessage() + name));
    }

    /**
     * Method makes {@link ResourceTemplate} be published.
     *
     * @param resourceTemplate of {@link ResourceTemplateDTO}
     * @throws ResourceTemplateIsPublishedException if resource template has been published already
     * @throws ResourceTemplateParameterListIsEmpty if resource template do not have attached parameters
     * @author Halyna Yatseniuk
     */
    public void publishResourceTemplate(ResourceTemplate resourceTemplate)
            throws ResourceTemplateIsPublishedException, ResourceTemplateParameterListIsEmpty {
        jooqDDL = new JooqDDL(dslContext);
        if (verifyIfResourceTemplateIsNotPublished(resourceTemplate) &&
                verifyIfResourceTemplateHasParameters(resourceTemplate)) {
            jooqDDL.createResourceContainerTable(resourceTemplate);
            resourceTemplate.setIsPublished(true);
            resourceTemplateRepository.save(resourceTemplate);
        }
    }

    /**
     * Method cancels {@link ResourceTemplate} publish.
     *
     * @param resourceTemplate of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    private void unPublishResourceTemplate(ResourceTemplate resourceTemplate) {
        jooqDDL = new JooqDDL(dslContext);
        if (verifyIfResourceTemplateIsPublished(resourceTemplate) &&
                verifyIfResourceTableIsEmpty(resourceTemplate)) {
            jooqDDL.dropResourceContainerTable(resourceTemplate);
            resourceTemplate.setIsPublished(false);
            resourceTemplateRepository.save(resourceTemplate);
        }
    }

    /**
     * Method verifies if {@link ResourceTemplate} table contains records.
     *
     * @param resourceTemplate of {@link ResourceTemplate}
     * @return true value if {@link ResourceTemplate} table is empty
     * @throws ResourceTemplateCanNotBeUnPublished if {@link ResourceTemplate} table contains records
     * @author Halyna Yatseniuk
     */
    public Boolean verifyIfResourceTableIsEmpty(ResourceTemplate resourceTemplate) {
        if (jooqDDL.countTableRecords(resourceTemplate) > 0) {
            throw new ResourceTemplateCanNotBeUnPublished(
                    ErrorMessage.RESOURCE_TEMPLATE_TABLE_CAN_NOT_BE_DROP.getMessage());
        }
        return true;
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
     * Method verifies if {@link ResourceTemplate} table name is unique.
     *
     * @param name of {@link ResourceTemplateDTO}
     * @return string of {@link ResourceTemplateDTO} table name if it is unique
     * @throws NotUniqueNameException if the resource template table name is not unique
     * @author Halyna Yatseniuk
     */
    private String verifyIfResourceTemplateTableNameIsUnique(String name) throws NotUniqueNameException {
        String generatedTableName = validator.generateTableOrColumnName(name);
        if (resourceTemplateRepository.findByTableName(generatedTableName).isPresent()) {
            throw new NotUniqueNameException(ErrorMessage.RESOURCE_TEMPLATE_TABLE_NAME_IS_NOT_UNIQUE.getMessage());
        }
        return generatedTableName;
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
     * Method verifies if {@link ResourceTemplate} has been published.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @return boolean true if {@link ResourceTemplateDTO} is published
     * @throws ResourceTemplateIsNotPublishedException if resource template has not been published
     * @author Halyna Yatseniuk
     */
    private Boolean verifyIfResourceTemplateIsPublished(ResourceTemplate resourceTemplate)
            throws ResourceTemplateIsNotPublishedException {
        if (!resourceTemplate.getIsPublished()) {
            throw new ResourceTemplateIsNotPublishedException(
                    ErrorMessage.RESOURCE_TEMPLATE_IS_NOT_PUBLISHED.getMessage());
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
