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

    private SearchImpl search;
    private SearchServiceImpl searchService;

    @Autowired
    public SearchController(SearchImpl search, SearchServiceImpl searchService) {
        this.search = search;
        this.searchService = searchService;
    }

    @PostMapping
    public ResponseEntity<List<ResourceTemplate>> searchContr(@RequestParam Map<String, Object> body) {
        return ResponseEntity.status(HttpStatus.OK).body(search.searchResourceTemp(body));

    }

    @GetMapping
    public ResponseEntity<List<ResourceTemplateDTO>> searching(@RequestParam(value = "search") String search) {


        return ResponseEntity.status(HttpStatus.OK).body(searchService.searchMethod());

    }
}
