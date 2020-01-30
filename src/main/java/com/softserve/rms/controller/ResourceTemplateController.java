package com.softserve.rms.controller;

import com.softserve.rms.dto.ResourceTemplateDTO;
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
     * The controller which saves a new {@link ResourceTemplateDTO}.
     *
     * @param templateDTO ResourceTemplateDTO
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @PostMapping("/resource-template")
    public ResponseEntity<ResourceTemplateDTO> save(@RequestBody ResourceTemplateDTO templateDTO) {
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
        LOG.info("Getting Resource Template by ID");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.getById(id));
    }

    /**
     * The controller which finds all {@link ResourceTemplateDTO} created by provided person id.
     *
     * @param personId of {@link Person}
     * @return list of {@link ResourceTemplateDTO} with appropriate person id
     * @author Halyna Yatseniuk
     */
    @GetMapping("/resource-templates/{personId}")
    public ResponseEntity<List<ResourceTemplateDTO>> getAllByPersonId(@PathVariable Long personId) {
        LOG.info("Getting all Resource Templates by User ID");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.getAllByPersonId(personId));
    }

    /**
     * The controller which updates a {@link ResourceTemplateDTO} by provided id.
     *
     * @param templateDTO ResourceTemplateDTO
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @PutMapping("/resource-template/{id}")
    public ResponseEntity<ResourceTemplateDTO> updateById
    (@PathVariable Long id, @RequestBody ResourceTemplateDTO templateDTO) {
        LOG.info("Updating Resource Template by ID");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.updateById(id, templateDTO));
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
        LOG.info("Deleting Resource Template by ID");
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
        LOG.info("Search a Resource Template by name or description contains");
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
        LOG.info("Publish a Resource Template");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.publishResourceTemplate(id));
    }
}