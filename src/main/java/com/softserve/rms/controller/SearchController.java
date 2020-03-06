package com.softserve.rms.controller;

import com.softserve.rms.dto.resourceRecord.ResourceRecordDTO;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.service.implementation.SearchServiceImpl;
import com.softserve.rms.util.ResourceFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    private SearchServiceImpl searchService;
    private ResourceFilterUtil resourceFilterUtil;

    @Autowired
    public SearchController(SearchServiceImpl searchService, ResourceFilterUtil resourceFilterUtil) {
        this.searchService = searchService;
        this.resourceFilterUtil = resourceFilterUtil;
    }

    @GetMapping("/{tableName}")
    public ResponseEntity<List<ResourceRecordDTO>> filterParameters(@RequestParam(value = "search") String search,
                                                                    @PathVariable String tableName) {
        List<SearchCriteria> searchCriteriaList = resourceFilterUtil.matchSearchCriteriaToPattern(search, tableName);
        return ResponseEntity.status(HttpStatus.OK).body(searchService.filterByCriteria(searchCriteriaList, tableName));
    }
}