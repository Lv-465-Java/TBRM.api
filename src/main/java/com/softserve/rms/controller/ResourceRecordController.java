package com.softserve.rms.controller;

import com.softserve.rms.dto.resourcerecord.ResourceRecordDTO;
import com.softserve.rms.dto.resourcerecord.ResourceRecordSaveDTO;
import com.softserve.rms.service.ResourceRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/resource-template/resource/{tableName}")
public class ResourceRecordController {
    private ResourceRecordService resourceRecordService;
    private static final Logger LOG = LoggerFactory.getLogger(ResourceRecordController.class);

    @Autowired
    public ResourceRecordController(ResourceRecordService resourceRecordService) {
        this.resourceRecordService = resourceRecordService;
    }

    @PostMapping
    public HttpStatus save(@PathVariable String tableName, @RequestBody ResourceRecordSaveDTO resourceDTO){
        LOG.info("Create a new Resource");
        resourceRecordService.save(tableName, resourceDTO);
        return HttpStatus.OK;
    }

    @GetMapping
    public ResponseEntity<List<ResourceRecordDTO>> findAll(@PathVariable String tableName) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceRecordService.findAll(tableName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceRecordDTO> findById(@PathVariable String tableName, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceRecordService.findByIdDTO(tableName, id));
    }

    @PatchMapping("/{id}")
    public HttpStatus update(@PathVariable String tableName, @PathVariable Long id,
                             @RequestBody ResourceRecordSaveDTO resourceRecordSaveDTO) {
        resourceRecordService.update(tableName, id, resourceRecordSaveDTO);
        return HttpStatus.OK;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String tableName, @PathVariable Long id) {
        resourceRecordService.delete(tableName, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
