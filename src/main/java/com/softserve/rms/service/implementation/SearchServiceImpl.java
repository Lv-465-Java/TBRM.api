package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.repository.implementation.JooqSearching;
import com.softserve.rms.service.SearchService;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.softserve.rms.constants.SearchOperation.*;
import static org.jooq.impl.DSL.field;

@Service
public class SearchServiceImpl implements SearchService {
    private JooqSearching jooqSearching;
    private DSLContext dslContext;
    private ModelMapper modelMapper;

    @Autowired
    public SearchServiceImpl(JooqSearching jooqSearching, DSLContext dslContext, ModelMapper modelMapper) {
        this.jooqSearching = jooqSearching;
        this.dslContext = dslContext;
        this.modelMapper = modelMapper;
    }

    public List<ResourceTemplateDTO> getSearchCriteria(List<SearchCriteria> criteriaList) {
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
        return filterByCriteria(conditionList);
    }

    public List<ResourceTemplateDTO> filterByCriteria(List<Condition> conditionList) {
        List<ResourceTemplate> resourceTemplates = jooqSearching.searchResourceTemplate(conditionList);
        return resourceTemplates.stream()
                .map(resourceTemplate -> modelMapper.map(resourceTemplate, ResourceTemplateDTO.class))
                .collect(Collectors.toList());
    }

//    public List<ResourceTemplateDTO> searchMethod() {
//        Field<Object> film = field("creator_id");
//        Field<Object> isPublished = field("is_published");
//
////        String myString = "is_published = false and creator_id = 5";
////        Condition withString = condition(myString);
////        Condition withField = field("name").containsIgnoreCase("Room");
////
////        List<Condition> list = new ArrayList<>();
////        list.add(withField);
////        list.add(withString);
//
//        Map<Field<?>, Object> map = new HashMap<>();
//        map.put(film, 5);
//        map.put(isPublished, true);
//        List<ResourceTemplate> resourceTemplates = jooqSearching.searchResourceTemplate(map);
//        return resourceTemplates.stream()
//                .map(resourceTemplate -> modelMapper.map(resourceTemplate, ResourceTemplateDTO.class))
//                .collect(Collectors.toList());
//    }
}