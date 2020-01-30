package com.softserve.rms.controller;

import com.softserve.rms.dto.ResourceParameterDTO;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceTemplate;
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

    /**
     * Constructor with parameters
     *
     * @author Andrii Bren
     */
    @Autowired
    public ResourceParameterController(ResourceParameterService resourceParameterService) {
        this.resourceParameterService = resourceParameterService;
    }

    /**
     * Controller which saves {@link ResourceParameter}.
     *
     * @param parameterDTO {@link ResourceParameterDTO}
     * @return {@link ResponseEntity} with generic type {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    @PostMapping
    public ResponseEntity<ResourceParameterDTO> save(@RequestBody ResourceParameterDTO parameterDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceParameterService.save(parameterDTO));
    }

    /**
     * Controller which gets {@link ResourceParameter} by {@link ResourceTemplate} id.
     *
     * @param templateId {@link ResourceTemplate} id
     * @return {@link ResponseEntity} with generic type list of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    @GetMapping("/byTemplateId/{templateId}")
    public ResponseEntity<List<ResourceParameterDTO>> getParametersByTemplateId(@PathVariable Long templateId) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceParameterService.getAllByTemplateId(templateId));
    }

    /**
     * Controller which gets {@link ResourceParameter} by id.
     *
     * @param parameterId {@link ResourceParameter} id
     * @return {@link ResponseEntity} with generic type {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    @GetMapping("/byId/{parameterId}")
    public ResponseEntity<ResourceParameterDTO> getOne(@PathVariable Long parameterId) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceParameterService.findByIdDTO(parameterId));
    }

    /**
     * Controller which gets all {@link ResourceParameter}.
     *
     * @return {@link ResponseEntity} with generic type list of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    @GetMapping
    public ResponseEntity<List<ResourceParameterDTO>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(resourceParameterService.getAll());
    }

    /**
     * Controller which updates {@link ResourceParameter}.
     *
     * @param parameterId  {@link ResourceParameter} id
     * @param parameterDTO {@link ResourceParameterDTO}
     * @return {@link ResponseEntity} with generic type {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    @PutMapping("/{parameterId}")
    public ResponseEntity<ResourceParameterDTO> update(@PathVariable Long parameterId,
                                                       @RequestBody ResourceParameterDTO parameterDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(resourceParameterService.update(parameterId, parameterDTO));
    }

    /**
     * Controller which deletes {@link ResourceParameter} by id.
     *
     * @param parameterId {@link ResourceParameter} id
     * @return {@link ResponseEntity} with generic type {@link Object}
     * @author Andrii Bren
     */
    @DeleteMapping
    public ResponseEntity<Object> delete(Long parameterId) {
        resourceParameterService.delete(parameterId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}