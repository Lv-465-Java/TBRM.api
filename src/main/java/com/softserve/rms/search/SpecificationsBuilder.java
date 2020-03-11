package com.softserve.rms.search;

import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpecificationsBuilder {
    private SearchAndFilterUtil searchAndFilterUtil;

    /**
     * Constructor with parameters
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public SpecificationsBuilder(SearchAndFilterUtil searchAndFilterUtil) {
        this.searchAndFilterUtil = searchAndFilterUtil;
    }

    /**
     * Method builds {@link Specification} with generic type {@link ResourceTemplate} by provided search parameters and
     * table name.
     *
     * @param search    URL string with parameters
     * @param tableName name of a table where entities are searched
     * @return list of {@link Specification} with generic type {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    public Specification<ResourceTemplate> buildSpecification(String search, String tableName) {
        List<SearchSpecification> specificationList = convertSearchCriteriaToSpecificationList(
                searchAndFilterUtil.matchSearchCriteriaToPattern(search, tableName));
        if (!specificationList.isEmpty()) {
            Specification<ResourceTemplate> result = specificationList.get(0);
            for (int i = 1; i < specificationList.size(); i++) {
                result = Specification.where(result).and(specificationList.get(i));
            }
            return result;
        }
        return null;
    }

    /**
     * Method converts list of {@link SearchCriteria} to list of {@link SearchSpecification}
     *
     * @param criteriaList list of {@link SearchCriteria}
     * @return list of {@link SearchSpecification}
     * @author Halyna Yatseniuk
     */
    public List<SearchSpecification> convertSearchCriteriaToSpecificationList(List<SearchCriteria> criteriaList) {
        return criteriaList.stream()
                .map(SearchSpecification::new)
                .collect(Collectors.toList());
    }
}