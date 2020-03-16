package com.softserve.rms.controller;

import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.dto.resourceParameter.ResourceParameterDTO;
import com.softserve.rms.dto.resourceParameter.ResourceParameterSaveDTO;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.service.ResourceParameterService;
import com.softserve.rms.service.ResourceTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/resource-template")
@RestController
public class ResourceTemplateController implements ResourceTemplateControllerApi {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceTemplateController.class);
    private ResourceTemplateService resourceTemplateService;
    private ResourceParameterService resourceParameterService;

    /**
     * Constructor with parameters.
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public ResourceTemplateController(ResourceTemplateService resourceTemplateService,
                                      ResourceParameterService resourceParameterService) {
        this.resourceTemplateService = resourceTemplateService;
        this.resourceParameterService = resourceParameterService;
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    public ResponseEntity<ResourceTemplateDTO> saveTemplate(ResourceTemplateSaveDTO templateDTO) {
        LOG.info("Creating a new Resource Template");
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceTemplateService.save(templateDTO));
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    public ResponseEntity<ResourceTemplateDTO> findTemplateById(Long templateId) {
        LOG.info("Getting Resource Template by ID: " + templateId);
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.findDTOById(templateId));
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    public ResponseEntity<Page<ResourceTemplateDTO>> findAllTemplates(Optional<Integer> page,
                                                                      Optional<Integer> pageSize) {
        LOG.info("Getting all Resource Templates");
        return ResponseEntity.status(HttpStatus.OK)
                .body(resourceTemplateService.getAll(page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5)));
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResponseEntity<Page<ResourceTemplateDTO>> findAllPublishedTemplates(Optional<Integer> page,
                                                                               Optional<Integer> pageSize) {
        LOG.info("Getting all published Resource Templates");
        return ResponseEntity.status(HttpStatus.OK)
                .body(resourceTemplateService.findAllPublishedTemplates(page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5)));
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResponseEntity<ResourceTemplateDTO> findTemplateByTableName(String tableName) {
        LOG.info("Getting Template by table name");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.findByTableNameDTO(tableName));
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    public ResponseEntity<Page<ResourceTemplateDTO>> findAllTemplatesByUserId(Long userId, Optional<Integer> page,
                                                                              Optional<Integer> pageSize) {
        LOG.info("Getting all Resource Templates by user ID: " + userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(resourceTemplateService.getAllByUserId(userId, page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5)));
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    public ResponseEntity<ResourceTemplateDTO> updateTemplateById(Long templateId, Map<String, Object> body) {
        LOG.info("Updating Resource Template by ID: " + templateId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(resourceTemplateService.checkIfTemplateCanBeUpdated(templateId, body));
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    public ResponseEntity<Object> deleteTemplateById(Long templateId) {
        LOG.info("Deleting Resource Template by ID: " + templateId);
        resourceTemplateService.checkIfTemplateCanBeDeleted(templateId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    public ResponseEntity<Boolean> publishResourceTemplate(Long templateId, Map<String, Object> body) {
        LOG.info("Publish a Resource Template by ID: " + templateId);
        resourceTemplateService.selectPublishOrCancelPublishAction(templateId, body);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Page<PrincipalPermissionDto>> getUsersWithAccess(String id, Optional<Integer> page,
                                                                           Optional<Integer> pageSize) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(resourceTemplateService.findPrincipalWithAccessToResourceTemplate(Long.parseLong(id),
                        page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5)));
    }

    @Override
    public ResponseEntity<PermissionDto> addPermissionToResourceTemplate(PermissionDto permissionDto, Principal principal) {
        resourceTemplateService.addPermissionToResourceTemplate(permissionDto, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<ChangeOwnerDto> changeOwnerForResourceTemplate(ChangeOwnerDto changeOwnerDto, Principal principal) {
        resourceTemplateService.changeOwnerForResourceTemplate(changeOwnerDto, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Object> deleteAceForCertainUser(PermissionDto permissionDto, Principal principal) {
        resourceTemplateService.closePermissionForCertainUser(permissionDto, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResponseEntity<ResourceParameterDTO> saveParameter(Long templateId, ResourceParameterSaveDTO parameterDTO) {
        LOG.info("Creating a new Resource Parameter");
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceParameterService.checkIfParameterCanBeSaved(templateId, parameterDTO));
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResponseEntity<Page<ResourceParameterDTO>> findParametersByTemplateId(Long templateId, Optional<Integer> page,
                                                                                 Optional<Integer> pageSize) {
        LOG.info("Getting Resource Parameter by Template ID: " + templateId);
        return ResponseEntity.status(HttpStatus.OK).body(resourceParameterService.findAllByTemplateId(templateId, page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5)));
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResponseEntity<ResourceParameterDTO> findParameterById(Long templateId, Long parameterId) {
        LOG.info("Getting Resource Parameter by Parameter ID: " + parameterId);
        return ResponseEntity.status(HttpStatus.OK).body(resourceParameterService.findByIdDTO(templateId, parameterId));
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResponseEntity<ResourceParameterDTO> updateParameterById(Long templateId, Long parameterId, ResourceParameterSaveDTO parameterDTO) {
        LOG.info("Updating Resource Parameter by Parameter ID: " + parameterId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(resourceParameterService.checkIfParameterCanBeUpdated(templateId, parameterId, parameterDTO));
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResponseEntity<Object> deleteParameterById(Long templateId, Long parameterId) {
        LOG.info("Deleting Resource Parameter by Parameter ID: " + parameterId);
        resourceParameterService.checkIfParameterCanBeDeleted(templateId, parameterId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}