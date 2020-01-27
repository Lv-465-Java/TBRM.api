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

@RestController
public class ResourceTemplateController {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceTemplateController.class);
    private ResourceTemplateService resourceTemplateService;

    @Autowired
    public ResourceTemplateController(ResourceTemplateService resourceTemplateService) {
        this.resourceTemplateService = resourceTemplateService;
    }

    @PostMapping("/resource-template")
    public ResponseEntity<ResourceTemplateDTO> createTemplate(@RequestBody ResourceTemplateDTO templateDTO) {
        LOG.info("Creating a new Resource Template");
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceTemplateService.createTemplate(templateDTO));
    }

    @GetMapping("/resource-template/{id}")
    public ResponseEntity<ResourceTemplateDTO> getTemplateById(@PathVariable Long id) {
        LOG.info("Getting Resource Template by ID");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.getTemplateById(id));
    }

    @GetMapping("/resource-templates/{userId}")
    public ResponseEntity<List<ResourceTemplateDTO>> getTemplatesByUserId(@PathVariable Long userId) {
        LOG.info("Getting all Resource Templates by User ID");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.getAllByUserId(userId));
    }

    @PutMapping("/resource-template/{id}")
    public ResponseEntity<ResourceTemplateDTO> updateTemplateById
            (@PathVariable Long id, @RequestBody ResourceTemplateDTO templateDTO) {
        LOG.info("Updating Resource Template by ID");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.updateTemplateById(id, templateDTO));
    }

    @DeleteMapping("/resource-template/{id}")
    public ResponseEntity deleteTemplateById(@PathVariable Long id) {
        LOG.info("Deleting Resource Template by ID");
        resourceTemplateService.deleteTemplateById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}