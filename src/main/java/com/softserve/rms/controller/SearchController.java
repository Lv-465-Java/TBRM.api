package com.softserve.rms.controller;

import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.repository.implementation.SearchImpl;
import com.softserve.rms.service.implementation.SearchServiceImpl;
import com.softserve.rms.util.ResourceSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/search")
public class SearchController {
    private SearchServiceImpl searchService;
    private ResourceSpecification resourceSpecification;

    @Autowired
    public SearchController(SearchServiceImpl searchService, ResourceSpecification resourceSpecification) {
        this.searchService = searchService;
        this.resourceSpecification = resourceSpecification;
    }

    @GetMapping("/{resourceTableName}")
    public ResponseEntity<List<ResourceTemplateDTO>> filterParameters(@RequestParam(value = "search") String search,
                                                                      @PathVariable String resourceTableName) {
        List<SearchCriteria> criteria = new ArrayList<>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>|=|!=)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
//            while (matcher.find()) {
//                criteria.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
//            }
            while (matcher.find()) {
                SearchCriteria searchCriteria = resourceSpecification.checkColumnParameterType(resourceTableName,
                        matcher.group(1), matcher.group(2), matcher.group(3));
                criteria.add(searchCriteria);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(searchService.getSearchCriteria(criteria));
    }
}