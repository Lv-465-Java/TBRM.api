package com.softserve.rms.repository;

import com.softserve.rms.entities.ResourceRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRelationRepository extends JpaRepository<ResourceRelation, Long> {
    ResourceRelation findByResourceParameterId(Long id);
}
