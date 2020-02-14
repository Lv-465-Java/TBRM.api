package com.softserve.rms.repository;

import com.softserve.rms.entities.ResourceTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceTemplateRepository extends JpaRepository<ResourceTemplate, Long> {



    @PreAuthorize("hasPermission(#id, 'com.softserve.rms.entities.ResourceTemplate', 'read')")
    Optional<ResourceTemplate> findById(Long id);

    @PreAuthorize("hasPermission(#id, 'com.softserve.rms.entities.ResourceTemplate', 'write')")
    void deleteById(Long id);

    @PostFilter("hasPermission(filterObject, 'read') or hasRole('MANAGER')")
    List<ResourceTemplate> findAll();

    /**
     * Method finds list of{@link ResourceTemplate} by user id.
     *
     * @param id of {@link ResourceTemplate}
     * @return list of {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    @PostFilter("hasPermission(filterObject, 'read')")
    List<ResourceTemplate> findAllByUserId(Long id);

    /**
     * Method finds list of{@link ResourceTemplate} by name or description.
     *
     * @param name        String
     * @param description String
     * @return list of {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    @PostFilter("hasPermission(filterObject, 'read')")
    List<ResourceTemplate> findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String name, String description);

    /**
     * Method finds {@link Optional<ResourceTemplate>} by name.
     *
     * @param name String
     * @return {@link Optional<ResourceTemplate>}
     * @author Halyna Yatseniuk
     */
    @PostFilter("hasPermission(#name, 'com.softserve.rms.entities.ResourceTemplate', 'read') or hasRole('MANAGER')")
    Optional<ResourceTemplate> findByName(String name);

    @PreAuthorize("hasPermission(#resourceTemplate, 'write')")
    ResourceTemplate save(ResourceTemplate resourceTemplate);
}