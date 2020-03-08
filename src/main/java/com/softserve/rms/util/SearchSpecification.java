package com.softserve.rms.util;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.constants.SearchOperation;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.InvalidParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class SearchSpecification implements Specification<ResourceTemplate> {

    private SearchCriteria criteria;

    @Autowired
    public SearchSpecification(SearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<ResourceTemplate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        switch (criteria.getOperation()) {
            case SearchOperation.EQUAL:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case SearchOperation.NOT_EQUAL:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case SearchOperation.GREATER_THAN:
                return builder.greaterThan(root.get(
                        criteria.getKey()), criteria.getValue().toString());
            case SearchOperation.LESS_THAN:
                return builder.lessThan(root.get(
                        criteria.getKey()), criteria.getValue().toString());
            case SearchOperation.CONTAINS:
                return builder.like(root.get(
                        criteria.getKey()), FieldConstants.PERCENTAGE.getValue() +
                        criteria.getValue() + FieldConstants.PERCENTAGE.getValue());
            default:
                throw new InvalidParametersException(ErrorMessage.WRONG_SEARCH_CRITERIA.getMessage());
        }
    }

    @Override
    public String toString() {
        return "SearchSpecification{" +
                "criteria=" + criteria +
                '}';
    }
}



//    public static Specification<Employee> getEmployeesByNameSpec(String name) {
//        return (root, query, criteriaBuilder) -> {
//            return criteriaBuilder.equal(root.get(Employee_.name), name);
//        };
//    }

//    public Specification<T> createSpecification(SearchCriteria searchCriteria) {
//
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue()
//    }

//    public List<Specification<T>> createSpecification(List<SearchCriteria> searchCriteria) {
//
//        return (root, query, criteriaBuilder) ->
//                criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue()
//    }