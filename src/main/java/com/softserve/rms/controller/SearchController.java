package com.softserve.rms.controller;

import com.softserve.rms.constants.HttpStatuses;
import com.softserve.rms.dto.resourceRecord.ResourceRecordDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.service.FilterService;
import com.softserve.rms.service.SearchService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class SearchController {
    private FilterService filterService;
    private SearchService searchService;

    /**
     * Constructor with parameters
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public SearchController(FilterService filterService, SearchService searchService) {
        this.filterService = filterService;
        this.searchService = searchService;
    }

    /**
     * The controller which finds {@link ResourceTemplateDTO} by provided search criteria.
     *
     * @param search URL string with search criteria
     * @return {@link ResponseEntity} with list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/search")
    public ResponseEntity<List<ResourceTemplateDTO>> searchEntity(@RequestParam(value = "search") String search) {
        return ResponseEntity.status(HttpStatus.OK).body(searchService.verifyIfSearchIsEmpty(search));
    }

    /**
     * The controller which finds {@link ResourceRecordDTO} by provided table name and search criteria.
     *
     * @param filter    URL string with search criteria
     * @param tableName name of a table where entities are searched
     * @return {@link ResponseEntity} with list of {@link ResourceRecordDTO}
     * @author Halyna Yatseniuk
     */
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HttpStatuses.OK),
            @ApiResponse(code = 401, message = HttpStatuses.UNAUTHORIZED),
            @ApiResponse(code = 400, message = HttpStatuses.BAD_REQUEST)
    })
    @GetMapping("/{tableName}/filter")
    public ResponseEntity<List<ResourceRecordDTO>> filterResourceByParameters(@RequestParam(value = "filter") String filter,
                                                                              @PathVariable String tableName) {
        return ResponseEntity.status(HttpStatus.OK).body(filterService.verifyIfFilterIsEmpty(filter, tableName));
    }
}