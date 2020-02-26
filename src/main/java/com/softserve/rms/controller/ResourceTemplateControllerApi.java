package com.softserve.rms.controller;

import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.entities.User;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface ResourceTemplateControllerApi {

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
    @PostMapping
    ResponseEntity<ResourceTemplateDTO> saveTemplate(@RequestBody ResourceTemplateSaveDTO templateDTO);

    /**
     * The controller which finds a {@link ResourceTemplateDTO} by provided id.
     *
     * @param templateId ResourceTemplateDTO id
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/{templateId}")
    ResponseEntity<ResourceTemplateDTO> findTemplateById(@PathVariable Long templateId);

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
    @GetMapping
    ResponseEntity<List<ResourceTemplateDTO>> findAllTemplates();

    /**
     * The controller which finds all published {@link ResourceTemplateDTO}.
     *
     * @return list of published {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN)
    })
    @GetMapping("/published")
    ResponseEntity<List<ResourceTemplateDTO>> findAllPublishedTemplates();

    /**
     * The controller which finds all {@link ResourceTemplateDTO} created by provided user id.
     *
     * @param userId {@link User} id
     * @return list of {@link ResourceTemplateDTO} with appropriate user id
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/user/{userId}")
    ResponseEntity<List<ResourceTemplateDTO>> findAllTemplatesByUserId(@PathVariable Long userId);

    /**
     * The controller which updates a {@link ResourceTemplateDTO} by provided id.
     *
     * @param templateId ResourceTemplateDTO id
     * @param body       map containing String key and Object value
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PatchMapping("/{templateId}")
    ResponseEntity<ResourceTemplateDTO> updateTemplateById(@PathVariable Long templateId, @RequestBody Map<String, Object> body);

    /**
     * The controller which deletes a {@link ResourceTemplateDTO} by provided id.
     *
     * @param templateId ResourceTemplateDTO id
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping("/{templateId}")
    ResponseEntity<Object> deleteTemplateById(@PathVariable Long templateId);

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
    ResponseEntity<List<ResourceTemplateDTO>> searchTemplateByNameOrDescription(@RequestParam String searchedWord);

    /**
     * The controller which publishes {@link ResourceTemplateDTO} by id.
     *
     * @param templateId of {@link ResourceTemplateDTO}
     * @return boolean value of {@link ResourceTemplateDTO} isPublished field
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PutMapping("/{templateId}/publish")
    ResponseEntity<Boolean> publishResourceTemplate(@PathVariable Long templateId, @RequestBody Map<String, Object> body);


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/permission/{id}")
    ResponseEntity<List<PrincipalPermissionDto>> getUsersWithAccess(@PathVariable String id);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping("/permission")
    ResponseEntity<PermissionDto> addPermissionToResourceTemplate(@RequestBody PermissionDto permissionDto, Principal principal);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @PostMapping("/permission/owner")
    ResponseEntity<ChangeOwnerDto> changeOwnerForResourceTemplate(@RequestBody ChangeOwnerDto changeOwnerDto, Principal principal);


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @DeleteMapping("/permission")
    ResponseEntity<Object> deleteAceForCertainUser(PermissionDto permissionDto, Principal principal);

}
