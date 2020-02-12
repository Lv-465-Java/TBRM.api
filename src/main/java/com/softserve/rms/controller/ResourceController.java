package com.softserve.rms.controller;

import com.softserve.rms.dto.resource.ResourceDTO;
import com.softserve.rms.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/resource")
@RestController
public class ResourceController {
    private ResourceService resourceService;
    private static final Logger LOG = LoggerFactory.getLogger(ResourceController.class);


    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public ResponseEntity<Object> save(ResourceDTO resourceDTO){
        LOG.info("Create a new Resource");
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.save(resourceDTO));
    }
}