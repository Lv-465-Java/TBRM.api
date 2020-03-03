package com.softserve.rms.util;

import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

import static com.softserve.rms.constants.SearchOperation.*;

public class ResourceSpecification implements Specification<ResourceRecord> {
//    private final List<SearchCriteria> searchCriteriaList;
    private SearchCriteria searchCriteria;

    //    @Autowired
    public ResourceSpecification(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
//        this.searchCriteriaList = new ArrayList<>();
    }

    @Override
    public Predicate toPredicate(final Root<ResourceRecord> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (searchCriteria.getOperation()) {
            case EQUAL:
                return builder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            case NEGATION:
                return builder.notEqual(root.get(searchCriteria.getKey()), searchCriteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString());
            case CONTAINS:
                return builder.like(root.get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
            default:
                return null;
        }
    }
}