package com.softserve.rms.controller;

import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.User;
import com.softserve.rms.service.ResourceTemplateService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/resource-template")
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
     * The controller which saves a new {@link ResourceTemplateSaveDTO}.
     *
     * @param templateDTO ResourceTemplateDTO
     * @return {@link ResourceTemplateSaveDTO}
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping("/")
    public ResponseEntity<ResourceTemplateDTO> save(@RequestBody ResourceTemplateSaveDTO templateDTO) {
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResourceTemplateDTO> getById(@PathVariable Long id) {
        LOG.info("Getting Resource Template by ID: " + id);
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.findDTOById(id));
    }

    /**
     * The controller which finds all {@link ResourceTemplateDTO}.
     *
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/")
    public ResponseEntity<List<ResourceTemplateDTO>> getAll() {
        LOG.info("Getting all Resource Templates");
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.getAll());
    }

    /**
     * The controller which finds all {@link ResourceTemplateDTO} created by provided user id.
     *
     * @param id of {@link User}
     * @return list of {@link ResourceTemplateDTO} with appropriate user id
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<List<ResourceTemplateDTO>> getAllByUserId(@PathVariable Long id) {
        LOG.info("Getting all Resource Templates by user ID: " + id);
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.getAllByUserId(id));
    }

    /**
     * The controller which updates a {@link ResourceTemplateDTO} by provided id.
     *
     * @param id   ResourceTemplateDTO
     * @param body map containing String key and Object value
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PatchMapping("/{id}")
    public ResponseEntity<ResourceTemplateDTO> updateById(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        LOG.info("Updating Resource Template by ID: " + id);
        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.updateById(id, body));
    }

    /**
     * The controller which deletes a {@link ResourceTemplateDTO} by provided id.
     *
     * @param id ResourceTemplateDTO
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        LOG.info("Deleting Resource Template by ID: " + id);
        resourceTemplateService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * The controller which searches all {@link ResourceTemplateDTO} by name or description.
     *
     * @param searchedWord request parameter to search resource templates
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/search")
    public ResponseEntity<List<ResourceTemplateDTO>> searchTemplateByNameOrDescription(@RequestParam String searchedWord) {
        LOG.info("Search a Resource Template by name or description contains: " + searchedWord);
        return ResponseEntity.status(HttpStatus.OK).body
                (resourceTemplateService.searchByNameOrDescriptionContaining(searchedWord));
    }

    /**
     * The controller which publishes {@link ResourceTemplateDTO} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return boolean value of {@link ResourceTemplateDTO} isPublished field
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PutMapping("/{id}/publish")
    public ResponseEntity<Boolean> publishResourceTemplate(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        LOG.info("Publish a Resource Template by ID: " + id);
        resourceTemplateService.selectPublishOrCancelPublishAction(id, body);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

//    /**
//     * The controller which cancels {@link ResourceTemplateDTO} publication by id.
//     *
//     * @param id of {@link ResourceTemplateDTO}
//     * @return boolean value of {@link ResourceTemplateDTO} isPublished field
//     * @author Halyna Yatseniuk
//     */
//    @ApiResponses(value = {
//            @ApiResponse(code = 200,message = HttpStatuses.OK),
//            @ApiResponse(code = 403,message = HttpStatuses.FORBIDDEN),
//            @ApiResponse(code = 401 ,message = HttpStatuses.UNAUTHORIZED),
//            @ApiResponse(code = 400 ,message = HttpStatuses.BAD_REQUEST)
//    })
//    @PostMapping("/unpublish-resource-template/{id}")
//    public ResponseEntity<Boolean> unPublishResourceTemplate(@PathVariable Long id) {
//        LOG.info("Canceling a Resource Template publish by ID: " + id);
//        return ResponseEntity.status(HttpStatus.OK).body(resourceTemplateService.unPublishResourceTemplate(id));
//    }
}