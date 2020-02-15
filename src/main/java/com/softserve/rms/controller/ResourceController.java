package com.softserve.rms.controller;

import com.softserve.rms.dto.resource.ResourceDTO;
import com.softserve.rms.dto.resource.ResourceSaveDTO;
import com.softserve.rms.entities.Resource;
import com.softserve.rms.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resource-template/resource/{resourceName}")
public class ResourceController {
    private ResourceService resourceService;
    private static final Logger LOG = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public HttpStatus save(@RequestBody ResourceSaveDTO resourceDTO){
        LOG.info("Create a new Resource");
        resourceService.save(resourceDTO);
        return HttpStatus.OK;
    }

    @GetMapping
    public ResponseEntity<List<ResourceDTO>> findAll(@PathVariable String resourceName) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.findAll(resourceName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDTO> findById(@PathVariable String resourceName, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.findById(resourceName, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String resourceName, @PathVariable Long id) {
        resourceService.delete(resourceName, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
