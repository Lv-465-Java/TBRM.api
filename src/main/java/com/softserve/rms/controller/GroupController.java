package com.softserve.rms.controller;

import com.softserve.rms.dto.group.GroupDto;
import com.softserve.rms.dto.group.GroupSaveDto;
import com.softserve.rms.dto.group.MemberDto;
import com.softserve.rms.dto.group.MemberOperationDto;
import com.softserve.rms.entities.Group;
import com.softserve.rms.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {
    private GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<GroupDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.getAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity<GroupDto> getByName(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.getByName(name));
    }

    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupSaveDto groupSaveDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.createGroup(groupSaveDto));
    }

    @PostMapping("/addMember")
    public ResponseEntity<MemberDto> addMember(@RequestBody MemberOperationDto member) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.addMember(member));
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteGroup(@PathVariable String name) {
        groupService.deleteCroup(name);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/deleteMember")
    public ResponseEntity<Object> deleteMember(@RequestBody MemberOperationDto memberDeleteDto) {
        groupService.deleteMember(memberDeleteDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
