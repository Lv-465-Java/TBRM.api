package com.softserve.rms.controller;

import com.softserve.rms.dto.group.GroupDto;
import com.softserve.rms.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupController {
    private GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/")
    public ResponseEntity<List<GroupDto>> getAll() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(groupService.getAll());
    }
}
