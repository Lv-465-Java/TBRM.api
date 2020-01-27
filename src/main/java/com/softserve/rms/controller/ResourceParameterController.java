package com.softserve.rms.controller;

import com.softserve.rms.dto.ResourceParameterDTO;
import com.softserve.rms.service.ResourceParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resource-parameter")
public class ResourceParameterController {
    private ResourceParameterService resourceParameterService;

    @Autowired
    public ResourceParameterController(ResourceParameterService resourceParameterService) {
        this.resourceParameterService = resourceParameterService;
    }

    @PostMapping
    public ResponseEntity<ResourceParameterDTO> createParameter(@RequestBody ResourceParameterDTO parameterDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceParameterService.create(parameterDTO));
    }

    @GetMapping("/byTemplateId/{templateId}")
    public ResponseEntity<List<ResourceParameterDTO>> getParametersByTemplateId(@PathVariable Long templateId) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceParameterService.getAllByTemplateId(templateId));
    }

    @GetMapping("/byId/{parameterId}")
    public ResponseEntity<ResourceParameterDTO> getOne(@PathVariable Long parameterId) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceParameterService.getOne(parameterId));
    }

    @PutMapping("/{parameterId}")
    public ResponseEntity<ResourceParameterDTO> update(@PathVariable Long parameterId,
                                                       @RequestBody ResourceParameterDTO parameterDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(resourceParameterService.update(parameterId, parameterDTO));
    }

    @DeleteMapping("/{parameterId")
    public ResponseEntity<Object> delete(@PathVariable Long parameterId) {
        resourceParameterService.delete(parameterId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
