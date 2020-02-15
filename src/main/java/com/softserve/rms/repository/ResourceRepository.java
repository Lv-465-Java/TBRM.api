package com.softserve.rms.repository;

import com.softserve.rms.dto.resource.ResourceDTO;
import com.softserve.rms.entities.Resource;
import org.jooq.DSLContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository {
//    Resource save(Resource resource);
    void save(Resource resource);

    void update(Resource resource);

    List<Resource> findAll(String resourceName);

    Resource findById(String resourceName, Long id);

    void delete(String resourceName, Long id);
}
