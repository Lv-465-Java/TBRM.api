package com.softserve.rms.controller;

import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.resourceparameter.ResourceParameterDTO;
import com.softserve.rms.dto.resourceparameter.ResourceParameterSaveDTO;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.service.ResourceParameterService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/resource-template/{id}/resource-parameter")
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
     * @param id           {@link ResourceTemplate} id
     * @param parameterDTO {@link ResourceParameterDTO}
     * @return {@link ResponseEntity} with generic type {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping
    public ResponseEntity<ResourceParameterDTO> save(@PathVariable Long id,
                                                     @RequestBody ResourceParameterSaveDTO parameterDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceParameterService.save(id, parameterDTO));
    }

    /**
     * Controller which finds {@link ResourceParameter} by {@link ResourceTemplate} id.
     *
     * @param id {@link ResourceTemplate} id
     * @return {@link ResponseEntity} with generic type list of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping
    public ResponseEntity<List<ResourceParameterDTO>> findParametersByTemplateId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceParameterService.findAllByTemplateId(id));
    }

    /**
     * Controller which finds {@link ResourceParameter} by id.
     *
     * @param id          {@link ResourceTemplate} id
     * @param parameterId {@link ResourceParameter} id
     * @return {@link ResponseEntity} with generic type {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/{parameterId}")
    public ResponseEntity<ResourceParameterDTO> findById(@PathVariable Long id,
                                                         @PathVariable Long parameterId) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceParameterService.findByIdDTO(id, parameterId));
    }

//    /**
//     * Controller which finds all {@link ResourceParameter}.
//     *
//     * @return {@link ResponseEntity} with generic type list of {@link ResourceParameterDTO}
//     * @author Andrii Bren
//     */
//    @ApiResponses(value = {
//            @ApiResponse(code = 200,message = HttpStatuses.OK),
//            @ApiResponse(code = 403,message = HttpStatuses.FORBIDDEN),
//            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
//            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
//    })
//    @GetMapping
//    public ResponseEntity<List<ResourceParameterDTO>> findAll() {
//        return ResponseEntity.status(HttpStatus.OK).body(resourceParameterService.findAll());
//    }

    /**
     * Controller which updates {@link ResourceParameter}.
     *
     * @param id           {@link ResourceTemplate} id
     * @param parameterId  {@link ResourceParameter} id
     * @param parameterDTO {@link ResourceParameterDTO}
     * @return {@link ResponseEntity} with generic type {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PutMapping("/{parameterId}")
    public ResponseEntity<ResourceParameterDTO> update(@PathVariable Long id,
                                                       @PathVariable Long parameterId,
                                                       @RequestBody ResourceParameterSaveDTO parameterDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(resourceParameterService.update(id, parameterId, parameterDTO));
    }

    /**
     * Controller which deletes {@link ResourceParameter} by id.
     *
     * @param id          {@link ResourceTemplate} id
     * @param parameterId {@link ResourceParameter} id
     * @return {@link ResponseEntity} with generic type {@link Object}
     * @author Andrii Bren
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping("/{parameterId}")
    public ResponseEntity<Object> delete(@PathVariable Long id, @PathVariable Long parameterId) {
        resourceParameterService.delete(id, parameterId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
