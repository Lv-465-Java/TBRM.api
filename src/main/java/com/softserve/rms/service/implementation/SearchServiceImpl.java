package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.resourceRecord.ResourceRecordDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.exceptions.InvalidParametersException;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.repository.implementation.DynamicSearchRepository;
import com.softserve.rms.service.SearchService;
import com.softserve.rms.search.ResourceFilterUtil;
import com.softserve.rms.search.SpecificationsBuilder;
import org.jooq.Condition;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.softserve.rms.constants.SearchOperation.*;
import static org.jooq.impl.DSL.field;

@Service
public class SearchServiceImpl implements SearchService {
    private DynamicSearchRepository dynamicSearchRepository;
    private ModelMapper modelMapper;
    private ResourceTemplateRepository templateRepository;
    private ResourceFilterUtil resourceFilterUtil;

    /**
     * Constructor with parameters
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public SearchServiceImpl(DynamicSearchRepository dynamicSearchRepository, ModelMapper modelMapper,
                             ResourceTemplateRepository templateRepository, ResourceFilterUtil resourceFilterUtil) {
        this.dynamicSearchRepository = dynamicSearchRepository;
        this.modelMapper = modelMapper;
        this.templateRepository = templateRepository;
        this.resourceFilterUtil = resourceFilterUtil;
    }

    /**
     * Method searches {@link ResourceTemplate} by provided search parameters.
     *
     * @param search    URL string with parameters
     * @param tableName name of a table where entities are searched
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    public List<ResourceTemplateDTO> searchBySpecification(String search, String tableName) {
        SpecificationsBuilder builder = new SpecificationsBuilder(resourceFilterUtil);

        Specification<ResourceTemplate> specification = builder.buildSpecification(search, tableName);
        List<ResourceTemplate> list = templateRepository.findAll(specification);
        return list.stream()
                .map(template -> modelMapper.map(template, ResourceTemplateDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Method filters {@link ResourceRecord} by provided search criteria.
     *
     * @param criteriaList list of {@link SearchCriteria}
     * @param tableName    name of a table where entities are searched
     * @return list of {@link ResourceRecordDTO}
     * @author Halyna Yatseniuk
     */
    public List<ResourceRecordDTO> filterByCriteria(List<SearchCriteria> criteriaList, String tableName) {
        List<ResourceRecord> resourceRecordList = dynamicSearchRepository.searchResourceTemplate(
                convertCriteriaToCondition(criteriaList), tableName);
        return resourceRecordList.stream()
                .map(resourceRecord -> modelMapper.map(resourceRecord, ResourceRecordDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Method converts {@link SearchCriteria} to {@link Condition} due to search criteria operation type.
     *
     * @param criteriaList list of {@link SearchCriteria}
     * @return list of {@link Condition}
     * @author Halyna Yatseniuk
     */
    public List<Condition> convertCriteriaToCondition(List<SearchCriteria> criteriaList) {
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
                    throw new InvalidParametersException(ErrorMessage.WRONG_SEARCH_CRITERIA.getMessage());
            }
        }
        return conditionList;
    }
}