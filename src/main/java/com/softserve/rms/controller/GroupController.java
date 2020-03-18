package com.softserve.rms.controller;

import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.PermissionDto;
import com.softserve.rms.dto.PrincipalPermissionDto;
import com.softserve.rms.dto.group.*;
import com.softserve.rms.dto.security.ChangeOwnerDto;
import com.softserve.rms.entities.Group;
import com.softserve.rms.service.GroupService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@RestController
@RequestMapping("/group")
public class GroupController {
    private GroupService groupService;

    /**
     * Constructor
     *
     * @param groupService {@link GroupService}
     */
    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * Method finds all {@link Group}.
     *
     * @param page current page
     * @param pageSize elements per page
     * @return {@link Page}
     * @author Artur Sydor
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping
    public ResponseEntity<Page<GroupDto>> getAll(@RequestParam Optional<Integer> page,
                                                 @RequestParam Optional<Integer> pageSize) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.getAll(page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5)));
    }

    /**
     * Method finds all group members by group id.
     *
     * @param groupId group id
     * @param page current page
     * @param pageSize elements per page
     * @return {@link Page}
     * @author Artur Sydor
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/member")
    public ResponseEntity<Page<MemberDto>> getAllMembers(@RequestParam Long groupId,
            @RequestParam Optional<Integer> page,@RequestParam Optional<Integer> pageSize) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.getAllMembers(groupId, page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5)));
    }

    /**
     * Method finds group by name.
     *
     * @param name of group
     * @return {@link GroupDto}
     * @author Artur Sydor
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("/{name}")
    public ResponseEntity<GroupDto> getByName(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.getByName(name));
    }

    /**
     * Method finds all users with access to group.
     *
     * @param id group id
     * @param page current page
     * @param pageSize elements per page
     * @return {@link Page}
     * @author Artur Sydor
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @GetMapping("permission/{id}")
    public ResponseEntity<Page<PrincipalPermissionDto>> getUsersWithAccess(@PathVariable Long id,
                @RequestParam Optional<Integer> page,@RequestParam Optional<Integer> pageSize) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.findPrincipalWithAccessToGroup(id, page.orElseGet(() -> 1), pageSize.orElseGet(() -> 5)));
    }

    /**
     * Method for saving new group.
     *
     * @param groupSaveDto group parameters
     * @return {@link GroupDto}
     * @author Artur Sydor
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupSaveDto groupSaveDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(groupService.createGroup(groupSaveDto));
    }

    /**
     * Methods save new user in group.
     *
     * @param member user parameters
     * @return {@link MemberDto}
     * @author Artur Sydor
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @PostMapping("/member")
    public ResponseEntity<MemberDto> addMember(@RequestBody MemberOperationDto member) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(groupService.addMember(member));
    }

    /**
     * Methods for adding write permissions on editing certain group.
     *
     * @param groupPermissionDto parameters needed to add permission
     * @param principal authenticated user
     * @author Artur Sydor
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @PostMapping("/permission")
    public ResponseEntity<Object> addPermission(@RequestBody GroupPermissionDto groupPermissionDto, Principal principal) {
        groupService.addWritePermission(groupPermissionDto, principal);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Methods for editing group.
     *
     * @param name group name
     * @param groupSaveDto parameters for update
     * @return {@link GroupDto}
     * @author Artur Sydor
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @PutMapping("/{name}")
    public ResponseEntity<GroupDto> editGroup(@PathVariable String name, @RequestBody GroupSaveDto groupSaveDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.update(name, groupSaveDto));
    }

    /**
     * Method for changing group owner.
     *
     * @param changeOwnerDto parameters needed to change owner
     * @param principal authenticated user
     * @author Artur Sydor
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @PutMapping("/owner")
    public ResponseEntity<Object> changeOwner(@RequestBody ChangeOwnerDto changeOwnerDto, Principal principal) {
        groupService.changeGroupOwner(changeOwnerDto, principal);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Method deletes group by name.
     *
     * @param name of group
     * @author Artur Sydor
     */
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HttpStatuses.NO_CONTENT),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @DeleteMapping("/{name}")
    public ResponseEntity<Object> deleteGroup(@PathVariable String name) {
        groupService.deleteCroup(name);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Methods deletes user from group.
     *
     * @param memberDeleteDto user parameters
     * @author Artur Sydor
     */
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HttpStatuses.NO_CONTENT),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @DeleteMapping("/member")
    public ResponseEntity<Object> deleteMember(@RequestBody MemberOperationDto memberDeleteDto) {
        groupService.deleteMember(memberDeleteDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Method for deleting permissions on group.
     *
     * @param groupPermissionDto parameters needed to add permission
     * @param principal authenticated user
     * @author Artur Sydor
     */
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = HttpStatuses.CREATED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST),
            @ApiResponse(code = 403, message = HttpStatuses.FORBIDDEN),
            @ApiResponse(code = 404, message = HttpStatuses.NOT_FOUND)
    })
    @DeleteMapping("/permission")
    public ResponseEntity<Object> deletePermission(@RequestBody GroupPermissionDto groupPermissionDto, Principal principal) {
        groupService.closePermissionForCertainUser(groupPermissionDto, principal);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
