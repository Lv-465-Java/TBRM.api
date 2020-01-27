package com.softserve.rms.repository;

import com.softserve.rms.entities.ResourceTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceTemplateRepository extends JpaRepository<ResourceTemplate, Long> {

    /**
     * Method find list of{@link ResourceTemplate} by user id.
     *
     * @param id of {@link ResourceTemplate}
     * @return list of {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    List<ResourceTemplate> findAllByUserId(Long id);

    /**
     * Method find list of{@link ResourceTemplate} by name or description.
     *
     * @param name of {@link ResourceTemplate}
     * @param description of {@link ResourceTemplate}
     * @return list of {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    List<ResourceTemplate> findByNameContainingOrDescriptionContaining(String name, String description);
}