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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resource-template/resource/{tableName}")
public class ResourceController {
    private ResourceService resourceService;
    private static final Logger LOG = LoggerFactory.getLogger(ResourceController.class);

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public HttpStatus save(@PathVariable String tableName, @RequestBody ResourceSaveDTO resourceDTO){
        LOG.info("Create a new Resource");
        resourceService.save(tableName, resourceDTO);
        return HttpStatus.OK;
    }

    @GetMapping
    public ResponseEntity<List<ResourceDTO>> findAll(@PathVariable String tableName) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.findAll(tableName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDTO> findById(@PathVariable String tableName, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceService.findByIdDTO(tableName, id));
    }

    @PatchMapping("/{id}")
    public HttpStatus update(@PathVariable String tableName, @PathVariable Long id,
                             @RequestBody Map<String, Object> body) {
        resourceService.update(tableName, id, body);
        return HttpStatus.OK;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String tableName, @PathVariable Long id) {
        resourceService.delete(tableName, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
