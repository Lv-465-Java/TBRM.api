package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.resourceRecord.ResourceRecordDTO;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.repository.implementation.JooqSearch;
import com.softserve.rms.service.SearchService;
import org.jooq.Condition;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.softserve.rms.constants.SearchOperation.*;
import static org.jooq.impl.DSL.field;

@Service
public class SearchServiceImpl implements SearchService {
    private JooqSearch jooqSearch;
    private ModelMapper modelMapper;

    @Autowired
    public SearchServiceImpl(JooqSearch jooqSearch, ModelMapper modelMapper) {
        this.jooqSearch = jooqSearch;
        this.modelMapper = modelMapper;
    }

    public List<ResourceRecordDTO> filterByCriteria(List<SearchCriteria> criteriaList, String tableName) {
        List<ResourceRecord> resourceRecordList = jooqSearch.searchResourceTemplate(
                getSearchCriteria(criteriaList), tableName);
        return resourceRecordList.stream()
                .map(resourceRecord -> modelMapper.map(resourceRecord, ResourceRecordDTO.class))
                .collect(Collectors.toList());
    }

    public List<Condition> getSearchCriteria(List<SearchCriteria> criteriaList) {
        List<Condition> conditionList = new ArrayList<>();
        for (SearchCriteria criteria : criteriaList) {
            switch (criteria.getOperation()) {
                case EQUAL:
                    Condition conditionWithEqualOperation = field(criteria.getKey()).eq(criteria.getValue());
                    conditionList.add(conditionWithEqualOperation);
                    break;
                case NOT_EQUAL:
                    Condition conditionWithNotEqualOperation = field(criteria.getKey()).ne(criteria.getValue());
                    conditionList.add(conditionWithNotEqualOperation);
                    break;
                case GREATER_THAN:
                    Condition conditionWithGreaterThanOperation = field(criteria.getKey()).greaterThan(criteria.getValue());
                    conditionList.add(conditionWithGreaterThanOperation);
                    break;
                case LESS_THAN:
                    Condition conditionWithLessThanOperation = field(criteria.getKey()).lessThan(criteria.getValue());
                    conditionList.add(conditionWithLessThanOperation);
                    break;
                case CONTAINS:
                    Condition conditionWithContainsOperation = field(criteria.getKey()).containsIgnoreCase(criteria.getValue());
                    conditionList.add(conditionWithContainsOperation);
                    break;
                default:
                    return null;
            }
        }
        return conditionList;
    }
}