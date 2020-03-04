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
//        List<SearchCriteria> criteria = new ArrayList<>();
//
//        if (search != null) {
//            List<String> searchStringList = Arrays.asList(search.split(","));
//            System.out.println(searchStringList);
//            for (String string : searchStringList) {
//                int matcherLength = string.length();
//                Pattern pattern = Pattern.compile(ValidationPattern.SEARCH_PATTERN);
//                Matcher matcher = pattern.matcher(string);
//                while (matcher.find()) {
//                    int neededLength = matcher.group(1).length() + matcher.group(2).length() + matcher.group(3).length();
//                    if (neededLength == matcherLength) {
//                        SearchCriteria searchCriteria = new SearchCriteria(
//                                matcher.group(1), matcher.group(2), matcher.group(3));
//                        criteria.add(searchCriteria);
//                    } else throw new RuntimeException("lalala");
//                }
//            }
//        }