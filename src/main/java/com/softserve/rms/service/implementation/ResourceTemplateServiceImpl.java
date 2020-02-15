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
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateCanNotBeModified;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateIsPublishedException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateParameterListIsEmpty;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.repository.implementation.JooqDDL;
import com.softserve.rms.service.PermissionManagerService;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.util.Validator;
import org.jooq.*;
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
    private PermissionManagerService permissionManagerService;
    private Validator validator = new Validator();
    private ModelMapper modelMapper = new ModelMapper();
    private DSLContext dslContext;
    private JooqDDL jooqDDL;

    /**
     * Constructor with parameters.
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public ResourceTemplateServiceImpl(ResourceTemplateRepository resourceTemplateRepository,
                                       UserRepository userRepository, PermissionManagerService permissionManagerService,
                                       DSLContext dslContext) {
        this.resourceTemplateRepository = resourceTemplateRepository;
        this.userRepository = userRepository;
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
        resourceTemplate.setUser(userRepository.findUserByEmail(principal.getName()).orElseThrow(() -> new RuntimeException("dd")));
        resourceTemplate.setIsPublished(false);
        Long resTempId = resourceTemplateRepository.saveAndFlush(resourceTemplate).getId();
        permissionManagerService.addPermissionForResourceTemplate(new PermissionDto(resTempId, principal.getName(), "write", true), principal);
        permissionManagerService.addPermissionForResourceTemplate(new PermissionDto(resTempId, "ROLE_MANAGER", "read", false), principal);
        return modelMapper.map(resourceTemplate, ResourceTemplateDTO.class);
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
    public ResourceTemplateDTO updateById(Long id, Map<String, Object> body)
            throws NotFoundException, NotUniqueNameException, ResourceTemplateCanNotBeModified {
        ResourceTemplate resourceTemplate = findEntityById(id);
        if (resourceTemplate.getIsPublished().equals(false)) {
            return updateResourceTemplateFields(resourceTemplate, body);
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
    public ResourceTemplateDTO updateResourceTemplateFields(ResourceTemplate resourceTemplate, Map<String, Object> body)
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
    public void deleteById(Long id) throws NotFoundException {
        if (findEntityById(id).getIsPublished().equals(false)) {
            delete(id);
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
    public void delete(Long id) {
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
     * Method makes {@link ResourceTemplate} be published.
     *
     * @param resourceTemplate of {@link ResourceTemplateDTO}
     * @throws ResourceTemplateIsPublishedException if resource template has been published already
     * @throws ResourceTemplateParameterListIsEmpty if resource template do not have attached parameters
     * @author Halyna Yatseniuk
     */
    private void publishResourceTemplate(ResourceTemplate resourceTemplate)
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
        //verifications
        resourceTemplate.setIsPublished(false);
        resourceTemplateRepository.save(resourceTemplate);
        //drop table
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