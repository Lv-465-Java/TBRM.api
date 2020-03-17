package com.softserve.rms.controller;

import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.resourceRecord.ResourceRecordDTO;
import com.softserve.rms.dto.resourceRecord.ResourceRecordSaveDTO;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.service.ResourceRecordService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/resource/{resourceName}")
public class ResourceRecordController {
    private ResourceRecordService resourceRecordService;
    private static final Logger LOG = LoggerFactory.getLogger(ResourceRecordController.class);

    /**
     * Constructor with parameters
     *
     * @author Andrii Bren
     */
    @Autowired
    public ResourceRecordController(ResourceRecordService resourceRecordService) {
        this.resourceRecordService = resourceRecordService;
    }

    /**
     * The controller which saves a dynamic {@link ResourceRecord} in a table specified
     * by the reсorder.
     *
     * @param resourceName   {@link ResourceTemplate} table name
     * @param resourceDTO instance of {@link ResourceRecordSaveDTO}
     * @return {@link ResponseEntity} with generic type {@link ResourceRecordDTO}
     * @author Andrii Bren
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping
    public HttpStatus save(@PathVariable String resourceName, @Valid @RequestBody ResourceRecordSaveDTO resourceDTO) {
        LOG.info("Create a new Resource");
        resourceRecordService.save(resourceName, resourceDTO);
        return HttpStatus.OK;
    }

    /**
     * Controller finds all dynamic {@link ResourceRecord} in a table specified
     * by the reсorder.
     *
     * @param resourceName {@link ResourceTemplate} table name
     * @return {@link ResponseEntity} with generic type {@link ResourceRecordDTO}
     * @author Andrii Bren
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping
    public ResponseEntity<Page<ResourceRecordDTO>> findAll(@PathVariable String resourceName,
                                                           @RequestParam Optional<Integer> page,
                                                           @RequestParam Optional<Integer> pageSize) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(resourceRecordService.findAll(resourceName, page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5)));
    }

    /**
     * Controller finds a dynamic {@link ResourceRecord} in a table specified
     * by the reсorder.
     *
     * @param resourceName {@link ResourceTemplate} table name
     * @param id        of {@link ResourceRecordDTO} id
     * @return {@link ResponseEntity} with generic type {@link ResourceRecordDTO}
     * @author Andrii Bren
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResourceRecordDTO> findById(@PathVariable String resourceName, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(resourceRecordService.findByIdDTO(resourceName, id));
    }

    /**
     * Controller which updates a dynamic {@link ResourceRecord}.
     *
     * @param resourceName             {@link ResourceTemplate} table name
     * @param id                    of {@link ResourceRecordDTO} id
     * @param resourceRecordSaveDTO instance of {@link ResourceRecordSaveDTO}
     * @return {@link ResponseEntity} with generic type {@link ResourceRecordDTO}
     * @author Andrii Bren
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PatchMapping("/{id}")
    public HttpStatus update(@PathVariable String resourceName, @PathVariable Long id,
                             @Valid @RequestBody ResourceRecordSaveDTO resourceRecordSaveDTO) {
        resourceRecordService.update(resourceName, id, resourceRecordSaveDTO);
        return HttpStatus.OK;
    }

    /**
     * Controller which deletes a dynamic {@link ResourceRecord} by id.
     *
     * @param resourceName {@link ResourceTemplate} table name
     * @param id        {@link ResourceRecordDTO} id
     * @return {@link ResponseEntity} with generic type {@link Object}
     * @author Andrii Bren
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable String resourceName, @PathVariable Long id) {
        resourceRecordService.delete(resourceName, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method for saving multiple photos
     *
     * @param files     to save.
     * @param resourceName {@link ResourceTemplate} table name
     * @param id        {@link ResourceRecordDTO} id
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PutMapping("/{id}/updatePhoto")
    public ResponseEntity changePhoto(@RequestPart List<MultipartFile> files,
                                      @PathVariable String resourceName,
                                      @PathVariable Long id) {
        files.stream().forEach(photo -> resourceRecordService.changePhoto(photo, resourceName, id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method for deleting all {@link ResourceRecord} photos
     *
     * @param resourceName {@link ResourceTemplate} table name
     * @param id        {@link ResourceRecordDTO} id
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping("/{id}/deletePhoto")
    public ResponseEntity deleteAllPhoto(@PathVariable String resourceName, @PathVariable Long id) {
        resourceRecordService.deleteAllPhotos(resourceName, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method for deleting specific photo of {@link ResourceRecord}
     *
     * @param resourceName {@link ResourceTemplate} table name
     * @param id        {@link ResourceRecordDTO} id
     * @param photo      particular photo
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping("/{id}/{photo}")
    public ResponseEntity deletePhoto(@PathVariable String resourceName, @PathVariable Long id, @PathVariable String photo) {
        resourceRecordService.deletePhoto(resourceName, id, photo);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method for deleting specific document of {@link ResourceRecord}
     *
     * @param resourceName {@link ResourceTemplate} table name
     * @param id        {@link ResourceRecordDTO} id
     * @param document
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping("/{id}/{document}")
    public ResponseEntity deleteDocument(@PathVariable String resourceName, @PathVariable Long id, @PathVariable String document) {
        resourceRecordService.deleteDocument(resourceName, id, document);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method for deleting all {@link ResourceRecord} documents
     *
     * @param resourceName {@link ResourceTemplate} table name
     * @param id        {@link ResourceRecordDTO} id
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @DeleteMapping("/{id}/deleteDocument")
    public ResponseEntity deleteAllDocuments(@PathVariable String resourceName, @PathVariable Long id) {
        resourceRecordService.deleteAllDocuments(resourceName, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method for saving multiple documents
     *
     * @param files     to save.
     * @param resourceName {@link ResourceTemplate} table name
     * @param id        {@link ResourceRecordDTO} id
     * @return {@link ResponseEntity}.
     * @author Mariia Shchur
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PutMapping("/{id}/document")
    public ResponseEntity uploadDocuments(@RequestPart List<MultipartFile> files,
                                      @PathVariable String resourceName,
                                      @PathVariable Long id) {
        files.stream().forEach(doc -> resourceRecordService.uploadDocument(doc, resourceName, id));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
