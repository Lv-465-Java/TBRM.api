package com.softserve.rms.controller;

import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.resourceRecord.ResourceRecordDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.service.implementation.SearchServiceImpl;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filter")
public class SearchController {
    private SearchServiceImpl searchService;

    /**
     * Constructor with parameters
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public SearchController(SearchServiceImpl searchService) {
        this.searchService = searchService;
    }

    /**
     * The controller which finds {@link ResourceRecordDTO} by provided table name and search criteria.
     * <p>
     * //     * @param filter    URL string with search criteria
     *
     * @param tableName name of a table where entities are searched
     * @return {@link ResponseEntity} with list of {@link ResourceRecordDTO}
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
//    @GetMapping("/{tableName}")
//    public ResponseEntity<List<ResourceRecordDTO>> filterResourceByParameters(@RequestParam(value = "filter") String filter,
//                                                                              @PathVariable String tableName) {
//        List<SearchCriteria> filterCriteriaList = resourceFilterUtil.matchSearchCriteriaToPattern(filter, tableName);
//        return ResponseEntity.status(HttpStatus.OK).body(searchService.filterByCriteria(filterCriteriaList, tableName));
//    }

    @GetMapping("/{tableName}")
    public ResponseEntity<List<ResourceTemplateDTO>> searchEntity(@RequestParam(value = "search") String search,
                                                                  @PathVariable String tableName) {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.check(search, tableName));
    }
}