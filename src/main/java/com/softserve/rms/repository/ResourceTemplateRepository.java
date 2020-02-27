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

    /**
     * Method finds {@link ResourceTemplate} by id
     *
     * @param id of{@link ResourceTemplate}
     * @return {@link Optional<ResourceTemplate>}
     * @author Halyna Yatseniuk
     */
    @PreAuthorize("hasPermission(#id, 'com.softserve.rms.entities.ResourceTemplate', 'read')" +
            "or hasRole('MANAGER')")
    Optional<ResourceTemplate> findById(Long id);

    /**
     * Method deletes {@link ResourceTemplate} by id
     *
     * @param id of{@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    @PreAuthorize("hasPermission(#id, 'com.softserve.rms.entities.ResourceTemplate', 'write')")
    void deleteById(Long id);

    /**
     * Method finds list of all {@link ResourceTemplate}.
     *
     * @return list of {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    @PostFilter("hasPermission(filterObject, 'read') or hasRole('MANAGER')")
    List<ResourceTemplate> findAll();

    /**
     * Method finds list of {@link ResourceTemplate} by user id.
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
     * @param name        searched word
     * @param description searched word
     * @return list of {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    @PostFilter("hasPermission(filterObject, 'read')")
    List<ResourceTemplate> findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String name, String description);

    /**
     * Method finds {@link Optional<ResourceTemplate>} by name.
     *
     * @param name of {@link ResourceTemplate}
     * @return {@link Optional<ResourceTemplate>}
     * @author Halyna Yatseniuk
     */
    @PreAuthorize("hasPermission(#name, 'com.softserve.rms.entities.ResourceTemplate', 'read') or hasRole('MANAGER')")
    Optional<ResourceTemplate> findByName(String name);

    /**
     * Method finds {@link Optional<ResourceTemplate>} by name with case ignore.
     *
     * @param name of {@link ResourceTemplate}
     * @return {@link Optional<ResourceTemplate>}
     * @author Halyna Yatseniuk
     */
    @PreAuthorize("hasPermission(#name, 'com.softserve.rms.entities.ResourceTemplate', 'read') or hasRole('MANAGER')")
    Optional<ResourceTemplate> findByNameIgnoreCase(String name);

    /**
     * Method finds {@link Optional<ResourceTemplate>} by table name.
     *
     * @param tableName of {@link ResourceTemplate}
     * @return {@link Optional<ResourceTemplate>}
     * @author Halyna Yatseniuk
     */
    @PreAuthorize("hasRole('MANAGER')")
    Optional<ResourceTemplate> findByTableName(String tableName);

    /**
     * Method creates a new {@link ResourceTemplate}.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @return created {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    @PreAuthorize("hasPermission(#resourceTemplate, 'write')")
    ResourceTemplate save(ResourceTemplate resourceTemplate);

    @PreAuthorize("hasRole('MANAGER')")
    ResourceTemplate saveAndFlush(ResourceTemplate resourceTemplate);

    @PreAuthorize("hasRole('MANAGER')")
    List<ResourceTemplate> findAllByIsPublishedIsTrue();


}
