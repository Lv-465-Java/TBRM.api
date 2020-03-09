package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.entities.ParameterType;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.NotDeletedException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.exceptions.PermissionException;
import com.softserve.rms.exceptions.resourseTemplate.*;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.repository.implementation.JooqDDL;
import com.softserve.rms.service.PermissionManagerService;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.util.Formatter;
import com.softserve.rms.util.Validator;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
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
    private Formatter formatter;

    private Logger Log = LoggerFactory.getLogger(ResourceTemplateServiceImpl.class);

    /**
     * Constructor with parameters.
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public ResourceTemplateServiceImpl(ResourceTemplateRepository resourceTemplateRepository,
                                       UserServiceImpl userService, PermissionManagerService permissionManagerService,
                                       DSLContext dslContext, JooqDDL jooqDDL, Formatter formatter) {
        this.resourceTemplateRepository = resourceTemplateRepository;
        this.userService = userService;
        this.permissionManagerService = permissionManagerService;
        this.dslContext = dslContext;
        this.jooqDDL = jooqDDL;
        this.formatter = formatter;
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO save(ResourceTemplateSaveDTO resourceTemplateSaveDTO)
            throws NotUniqueNameException {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        ResourceTemplate resourceTemplate = new ResourceTemplate();
        resourceTemplate.setName(verifyIfResourceTemplateNameIsUnique(resourceTemplateSaveDTO.getName()));
        resourceTemplate.setTableName(verifyIfResourceTemplateTableNameIsUnique(resourceTemplateSaveDTO.getName()));
        resourceTemplate.setDescription(resourceTemplateSaveDTO.getDescription());
        resourceTemplate.setUser(userService.getUserByEmail(principal.getName()));
        resourceTemplate.setIsPublished(false);
        Long resTempId = resourceTemplateRepository.saveAndFlush(resourceTemplate).getId();
        setAccessToTemplate(resTempId, principal);
        return modelMapper.map(resourceTemplate, ResourceTemplateDTO.class);
    }

    /**
     * Method sets "write" access principle to created {@link ResourceTemplate}.
     *
     * @param resTempId of {@link ResourceTemplateDTO}
     * @param principal of currently authenticated user
     * @author Marian Dutchyn
     */
    public void setAccessToTemplate(Long resTempId, Principal principal) {
        permissionManagerService.addPermission(
                new PermissionDto(resTempId, principal.getName(), "write", true), principal, ResourceTemplate.class);
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
     * @author Andrii Bren
     */
    @Override
    public List<ResourceTemplateDTO> findAllPublishedTemplates() {
        List<ResourceTemplate> resourceTemplates = resourceTemplateRepository.findAllByIsPublishedIsTrue();
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
            Principal principal = SecurityContextHolder.getContext().getAuthentication();
            permissionManagerService.closeAllPermissions(id, principal, ResourceTemplate.class);
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
    public ResourceTemplate findByTableName(String name) {
        return resourceTemplateRepository.findByTableName(name)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.CAN_NOT_FIND_A_RESOURCE_TABLE.getMessage() + name));
    }

    @Override
    public ResourceTemplateDTO findByTableNameDTO(String name) {
        return modelMapper
                .map(findByTableName(name), ResourceTemplateDTO.class);
    }

    /**
     * Method makes {@link ResourceTemplate} be published.
     *
     * @param resourceTemplate of {@link ResourceTemplateDTO}
     * @throws ResourceTemplateIsPublishedException if resource template has been published already
     * @throws ResourceTemplateParameterListIsEmpty if resource template do not have attached parameters
     * @author Halyna Yatseniuk
     */
    private void publishResourceTemplate(ResourceTemplate resourceTemplate)
            throws ResourceTemplateIsPublishedException, ResourceTemplateParameterListIsEmpty {
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
        if (verifyIfResourceTemplateIsPublished(resourceTemplate) &&
                verifyIfResourceTableCanBeDropped(resourceTemplate)) {
            jooqDDL.dropResourceContainerTable(resourceTemplate);
            resourceTemplate.setIsPublished(false);
            resourceTemplateRepository.save(resourceTemplate);
        }
    }

    /**
     * Method verifies if {@link ResourceTemplate} table contains records or have references to it.
     *
     * @param resourceTemplate of {@link ResourceTemplate}
     * @return true value if {@link ResourceTemplate} table is empty and do not have references to it
     * @throws ResourceTemplateCanNotBeUnPublished if {@link ResourceTemplate} table contains records or
     *                                             has at least one reference to it
     * @author Halyna Yatseniuk
     */
    public Boolean verifyIfResourceTableCanBeDropped(ResourceTemplate resourceTemplate) {
        if (jooqDDL.countTableRecords(resourceTemplate) > 0) {
            throw new ResourceTemplateCanNotBeUnPublished(
                    ErrorMessage.RESOURCE_TEMPLATE_TABLE_CAN_NOT_BE_DROPPED.getMessage());
        } else if (jooqDDL.countReferencesToTable(resourceTemplate)) {
            throw new ResourceTemplateCanNotBeUnPublished(
                    ErrorMessage.RESOURCE_TEMPLATE_TABLE_CAN_NOT_BE_DELETED.getMessage());
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
     * Method returns list of principal with permissions to object
     *
     * @param id of {@link ResourceTemplate}
     * @return List of principal with access to object
     * @author Marian Dutchyn
     */
    @Override
    public List<PrincipalPermissionDto> findPrincipalWithAccessToResourceTemplate(Long id) {
        return permissionManagerService.findPrincipalWithAccess(id, ResourceTemplate.class);
    }

    /**
     * Method add/update permission on @{@link ResourceTemplate}
     *
     * @param permissionDto {@link PermissionDto}
     * @param principal     authenticated user
     * @author Marian Dutchyn
     */
    @Override
    public void addPermissionToResourceTemplate(PermissionDto permissionDto, Principal principal) {
        permissionManagerService.addPermission(permissionDto, principal, ResourceTemplate.class);
    }

    /**
     * Method changes owner for {@link ResourceTemplate}
     *
     * @param changeOwnerDto {@link ChangeOwnerDto}
     * @param principal      authenticated user
     * @throws PermissionException
     * @author Marian Dutchyn
     */
    @Override
    public void changeOwnerForResourceTemplate(ChangeOwnerDto changeOwnerDto, Principal principal) {
        permissionManagerService.changeOwner(changeOwnerDto, principal, ResourceTemplate.class);
    }

    @Override
    public void closePermissionForCertainUser(PermissionDto permissionDto, Principal principal) {
        permissionManagerService.closePermissionForCertainUser(permissionDto, principal, ResourceTemplate.class);
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
    public Boolean verifyIfResourceTemplateHasParameters(ResourceTemplate resourceTemplate)
            throws ResourceTemplateParameterListIsEmpty {
        if (resourceTemplate.getResourceParameters().isEmpty()) {
            throw new ResourceTemplateParameterListIsEmpty
                    (ErrorMessage.RESOURCE_TEMPLATE_DO_NOT_HAVE_ANY_PARAMETERS.getMessage());
        } else {
            verifyIfReferencedTemplateIsPublished(resourceTemplate);
        }
        return true;
    }

    /**
     * Method verifies if any parameter of {@link ResourceTemplate} references to unpublished template.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @throws ResourceTemplateIsNotPublishedException if resource template do not have attached parameters
     * @author Halyna Yatseniuk
     */
    private void verifyIfReferencedTemplateIsPublished(ResourceTemplate resourceTemplate)
            throws ResourceTemplateIsNotPublishedException {
        List<ResourceTemplate> list = resourceTemplate.getResourceParameters().stream()
                .filter(parameter -> parameter.getParameterType().equals(ParameterType.POINT_REFERENCE))
                .filter(parameter -> parameter.getResourceRelations().getRelatedResourceTemplate().getIsPublished().equals(false))
                .map(parameter -> parameter.getResourceRelations().getRelatedResourceTemplate())
                .collect(Collectors.toList());
        if (!list.isEmpty()) {
            throw new ResourceTemplateIsNotPublishedException(
                    ErrorMessage.RESOURCE_TEMPLATE_CAN_NOT_BE_PUBLISHED.getMessage()
                            + formatter.errorMessageFormatter(list));
        }
    }
}
