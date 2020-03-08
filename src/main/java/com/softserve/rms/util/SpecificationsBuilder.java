package com.softserve.rms.util;

import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.InvalidParametersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SpecificationsBuilder {

    private ResourceFilterUtil resourceFilterUtil;

    @Autowired
    public SpecificationsBuilder(ResourceFilterUtil resourceFilterUtil) {
        this.resourceFilterUtil = resourceFilterUtil;
    }

//    public SpecificationsBuilder with(String key, String operation, Object value) {
//        searchCriteriaList.add(new SearchCriteria(key, operation, value));
//        return this;
//    }

    public List<SearchSpecification> convertSearchCriteriaToSpecificationList(List<SearchCriteria> criteriaList) {
        return criteriaList.stream()
                .map(SearchSpecification::new)
                .collect(Collectors.toList());
    }

    //count error
    public Specification<ResourceTemplate> buildSpecification(String search, String tableName) {
        List<SearchCriteria> specificationList =
                resourceFilterUtil.matchSearchCriteriaToPattern(search, tableName);
        if (!specificationList.isEmpty()) {
            if (tableName.equals("resource_templates")) {
                Specification<ResourceTemplate> result = new SearchSpecification(specificationList.get(0));
                for (int i = 1; i < specificationList.size(); i++) {
                    result = Specification.where(result).and(new SearchSpecification(specificationList.get(i)));
                }
                return result;
            }
        }
        return null;
    }


//    public Specification<?> build() {
//        if (searchCriteriaList.size() == 0) {
//            return null;
//        }

//        List<Specification<?>> specs = searchCriteriaList.stream()
//                .map(SearchSpecification::new)
//                .collect(Collectors.toList());
//
//        Specification<?> result = specs.get(0);
//
//        for (int i = 1; i < searchCriteriaList.size(); i++) {
//            result = searchCriteriaList.get(i)
//                    .isOrPredicate()
//                    ? Specification.where(result)
//                    .or(specs.get(i))
//                    : Specification.where(result)
//                    .and(specs.get(i));
//        }
//        return result;
//    }

}