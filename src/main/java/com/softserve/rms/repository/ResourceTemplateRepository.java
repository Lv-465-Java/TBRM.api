package com.softserve.rms.repository;

import com.softserve.rms.entities.ResourceTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceTemplateRepository extends JpaRepository<ResourceTemplate, Long> {

    /**
     * Method find list of{@link ResourceTemplate} by user id.
     *
     * @param id of {@link ResourceTemplate}
     * @return list of {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    List<ResourceTemplate> findAllByPersonId(Long id);

    /**
     * Method find list of{@link ResourceTemplate} by name or description.
     *
     * @param name        String
     * @param description String
     * @return list of {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    List<ResourceTemplate> findByTableNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);

    Optional<ResourceTemplate> findByName(String name);
}