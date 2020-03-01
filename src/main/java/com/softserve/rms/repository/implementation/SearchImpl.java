package com.softserve.rms.repository.implementation;

import com.softserve.rms.dto.search.SearchCriteriaDTO;
import com.softserve.rms.entities.ResourceTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SearchImpl {
    @PersistenceContext
    private EntityManager entityManager;

    public List<ResourceTemplate> searchResourceTemp(Map<String, Object> params){
//        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<ResourceTemplate> query = builder.createQuery(ResourceTemplate.class);
//        Root r = query.from(ResourceTemplate.class);
//
//        Predicate predicate = builder.conjunction();
//
//        UserSearchQueryCriteriaConsumer searchConsumer =
//                new UserSearchQueryCriteriaConsumer(predicate, builder, r);
//        params.stream().forEach(searchConsumer);
//        predicate = searchConsumer.getPredicate();
//        query.where(predicate);
//
//        List<ResourceTemplate> result = entityManager.createQuery(query).getResultList();
//        return result;


        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ResourceTemplate> cq = cb.createQuery(ResourceTemplate.class);

        Root<ResourceTemplate> book = cq.from(ResourceTemplate.class);
        List<Predicate> predicates = new ArrayList<>();

        if (params.get("name") != null) {
            predicates.add(cb.equal(book.get("name"), params.values()));
        }
//        if (title != null) {
//            predicates.add(cb.like(book.get("title"), "%" + title + "%"));
//        }
        cq.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(cq).getResultList();
    }
}
