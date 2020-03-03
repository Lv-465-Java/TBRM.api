package com.softserve.rms.controller;

import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.repository.implementation.SearchImpl;
import com.softserve.rms.service.implementation.SearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchController {
    private SearchServiceImpl searchService;

    @Autowired
    public SearchController(SearchServiceImpl searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public ResponseEntity<List<ResourceTemplateDTO>> searching() {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.searchMethod());
    }

//    @RequestParam(value = "search") String search,
//    @PathVariable String tableName)
}