package com.softserve.rms.controller;

import com.softserve.rms.dto.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
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

    @Autowired
    public ResourceTemplateController(ResourceTemplateService resourceTemplateService) {
        this.resourceTemplateService = resourceTemplateService;
    }

    @PostMapping("/resource-template")
    public ResponseEntity<ResourceTemplateDTO> create(@RequestBody ResourceTemplateDTO templateDTO) {
        LOG.info("Creating a new Resource Template");
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceTemplateService.create(templateDTO));
    }

    @GetMapping("/resource-template/{id}")
    public ResponseEntity<ResourceTemplateDTO> getById(@PathVariable Long id) {
        LOG.info("Getting Resource Template by ID");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.getById(id));
    }

    @GetMapping("/resource-templates/{userId}")
    public ResponseEntity<List<ResourceTemplateDTO>> getAllByUserId(@PathVariable Long userId) {
        LOG.info("Getting all Resource Templates by User ID");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.getAllByUserId(userId));
    }

    @PutMapping("/resource-template/{id}")
    public ResponseEntity<ResourceTemplateDTO> updateById
            (@PathVariable Long id, @RequestBody ResourceTemplateDTO templateDTO) {
        LOG.info("Updating Resource Template by ID");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.updateById(id, templateDTO));
    }

    @DeleteMapping("/resource-template/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        LOG.info("Deleting Resource Template by ID");
        resourceTemplateService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/search-resource-template")
    public ResponseEntity<List<ResourceTemplateDTO>> searchTemplateByNameOrDescription(@RequestBody Map<String, String> body) {
        LOG.info("Search a Resource Template by name or description contains");
        return ResponseEntity.status(HttpStatus.OK).body
                (resourceTemplateService.searchByNameOrDescriptionContaining(body));
    }
}