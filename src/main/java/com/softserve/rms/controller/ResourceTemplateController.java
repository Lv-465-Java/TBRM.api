package com.softserve.rms.controller;

import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.Person;
import com.softserve.rms.service.ResourceTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ResourceTemplateController {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceTemplateController.class);
    private ResourceTemplateService resourceTemplateService;

    /**
     * Constructor with parameters.
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public ResourceTemplateController(ResourceTemplateService resourceTemplateService) {
        this.resourceTemplateService = resourceTemplateService;
    }

    /**
     * The controller which saves a new {@link ResourceTemplateSaveDTO}.
     *
     * @param templateDTO ResourceTemplateDTO
     * @return {@link ResourceTemplateSaveDTO}
     * @author Halyna Yatseniuk
     */
    @PostMapping("/resource-template")
    public ResponseEntity<ResourceTemplateDTO> save(@RequestBody ResourceTemplateSaveDTO templateDTO) {
        LOG.info("Creating a new Resource Template");
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceTemplateService.save(templateDTO));
    }

    /**
     * The controller which finds a {@link ResourceTemplateDTO} by provided id.
     *
     * @param id ResourceTemplateDTO
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @GetMapping("/resource-template/{id}")
    public ResponseEntity<ResourceTemplateDTO> getById(@PathVariable Long id) {
        LOG.info("Getting Resource Template by ID: " + id);
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.findDTOById(id));
    }

    /**
     * The controller which finds all {@link ResourceTemplateDTO}.
     *
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @GetMapping("/resource-template")
    public ResponseEntity<List<ResourceTemplateDTO>> getAll() {
        LOG.info("Getting all Resource Templates");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.getAll());
    }

    /**
     * The controller which finds all {@link ResourceTemplateDTO} created by provided user id.
     *
     * @param userId of {@link Person}
     * @return list of {@link ResourceTemplateDTO} with appropriate user id
     * @author Halyna Yatseniuk
     */
    @GetMapping("/resource-templates/{userId}")
    public ResponseEntity<List<ResourceTemplateDTO>> getAllByUserId(@PathVariable Long userId) {
        LOG.info("Getting all Resource Templates by user ID: " + userId);
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.getAllByUserId(userId));
    }

    /**
     * The controller which updates a {@link ResourceTemplateDTO} by provided id.
     *
     * @param id   ResourceTemplateDTO
     * @param body map containing String key and Object value
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @PatchMapping("/resource-template/{id}")
    public ResponseEntity<ResourceTemplateDTO> updateById(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        LOG.info("Updating Resource Template by ID: " + id);
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.updateById(id, body));
    }

    /**
     * The controller which deletes a {@link ResourceTemplateDTO} by provided id.
     *
     * @param id ResourceTemplateDTO
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @DeleteMapping("/resource-template/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        LOG.info("Deleting Resource Template by ID: " + id);
        resourceTemplateService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * The controller which searches all {@link ResourceTemplateDTO} by name or description.
     *
     * @param body map containing String key and String value
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @GetMapping("/search-resource-template")
    public ResponseEntity<List<ResourceTemplateDTO>> searchTemplateByNameOrDescription(@RequestBody Map<String, String> body) {
        LOG.info("Search a Resource Template by name or description contains: " + body.get("search"));
        return ResponseEntity.status(HttpStatus.OK).body
                (resourceTemplateService.searchByNameOrDescriptionContaining(body));
    }

    /**
     * The controller which publishes {@link ResourceTemplateDTO} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return boolean value of {@link ResourceTemplateDTO} isPublished field
     * @author Halyna Yatseniuk
     */
    @PostMapping("/publish-resource-template/{id}")
    public ResponseEntity<Boolean> publishResourceTemplate(@PathVariable Long id) {
        LOG.info("Publish a Resource Template by ID: " + id);
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.publishResourceTemplate(id));
    }

    /**
     * The controller which cancels {@link ResourceTemplateDTO} publication by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return boolean value of {@link ResourceTemplateDTO} isPublished field
     * @author Halyna Yatseniuk
     */
    @PostMapping("/unpublish-resource-template/{id}")
    public ResponseEntity<Boolean> unPublishResourceTemplate(@PathVariable Long id) {
        LOG.info("Canceling a Resource Template publish by ID: " + id);
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.unPublishResourceTemplate(id));
    }
}