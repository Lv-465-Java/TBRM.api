package com.softserve.rms.controller;

import com.softserve.rms.dto.resourceRecord.ResourceRecordDTO;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.service.implementation.SearchServiceImpl;
import com.softserve.rms.util.ResourceSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
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

    @GetMapping("/{tableName}")
    public ResponseEntity<List<ResourceRecordDTO>> filterParameters(@RequestParam(value = "search") String search,
                                                                    @PathVariable String tableName) {
        List<SearchCriteria> criteria = new ArrayList<>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>|=|!=)(\\w+?),");
            String str = "(\\w+?)(:|<|>|=|!=)(\\w+?(\\.w+?))";
            String numb = "(\\w+?)(:|<|>|=|!=)(\\w+?|\\d+?\\.\\d+?)";
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                SearchCriteria searchCriteria = resourceSpecification.checkColumnParameterType(tableName,
                        matcher.group(1), matcher.group(2), matcher.group(3));
                criteria.add(searchCriteria);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(searchService.filterByCriteria(criteria, tableName));
    }

    public static void main(String[] args) {
        String numb = "user_id=34.67";
        String string = "(\\w+?)(:|<|>|=|!=)(\\w+?|[0-9]+(\\.[0-9]+)?)";
//        String string = "(\\w+?)(:|<|>|=|!=)(\\w+?|\\d+?\\.[\\d+?])";
        Pattern pattern = Pattern.compile(string);
        Matcher matcher = pattern.matcher(numb);

        System.out.println(matcher.find());
    }

    //            while (matcher.find()) {
//                criteria.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
//            }
}