package com.softserve.rms.search;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.constants.SearchOperation;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.exceptions.InvalidParametersException;
import com.softserve.rms.exceptions.SqlGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


public class SearchSpecification implements Specification<ResourceTemplate> {
    private SearchCriteria criteria;

    /**
     * Constructor with parameters
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public SearchSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    /**
     * Method builds a WHERE clause for a query of the {@link ResourceTemplate} entity in form of a {@link Predicate}
     * for the given {@link Root} and {@link CriteriaQuery}.
     *
     * @param root    must not be {@literal null}.
     * @param query   must not be {@literal null}.
     * @param builder must not be {@literal null}.
     * @return a {@link Predicate}, may be {@literal null}.
     * @author Halyna Yatseniuk
     */
    @Override
    public Predicate toPredicate(Root<ResourceTemplate> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        try {
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
                    throw new InvalidParametersException("jjjj");
            }
        } catch (IllegalArgumentException ex) {
            throw new SqlGrammarException(ErrorMessage.INVALID_COLUMN_CRITERIA.getMessage());
        }
    }
}