package com.softserve.rms.repository;

import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.util.PermissionChecker;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * This class is wrapper for ResourceTemplateRepositoryHelper,
 * which checks permissions for ResourceTemplate.
 */
@Component
public class ResourceTemplateRepository  {
    private ResourceTemplateRepositoryHelper repositoryHelper;
    private PermissionChecker permissionChecker;

    public ResourceTemplateRepository(ResourceTemplateRepositoryHelper repositoryHelper,
                                      PermissionChecker permissionChecker) {
        this.repositoryHelper = repositoryHelper;
        this.permissionChecker = permissionChecker;
    }

    public Optional<ResourceTemplate> findById(Long id) {
        return repositoryHelper.findById(id);
    }

    public void deleteById(Long id) {
        repositoryHelper.deleteById(id);
    }

    public List<ResourceTemplate> findAll() {
        return repositoryHelper.findAll();
    }

    public List<ResourceTemplate> findAll(Specification<ResourceTemplate> specification) {
        return repositoryHelper.findAll(specification);
    }

    public List<ResourceTemplate> findAllByUserId(Long id) {
        return repositoryHelper.findAllByUserId(id);
    }

    public List<ResourceTemplate> findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String name, String description) {
        return repositoryHelper.findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(name, description);
    }

    public Optional<ResourceTemplate> findByName(String name) {
        Optional<ResourceTemplate> resourceTemplate = repositoryHelper.findByName(name);
        resourceTemplate.ifPresent(template -> permissionChecker.checkReadPermission(template));
        return resourceTemplate;
    }

    public Optional<ResourceTemplate> findByNameIgnoreCase(String name) {
        Optional<ResourceTemplate> resourceTemplate = repositoryHelper.findByNameIgnoreCase(name);
        resourceTemplate.ifPresent(template -> permissionChecker.checkReadPermission(template));
        return resourceTemplate;
    }

    public Optional<ResourceTemplate> findByTableName(String tableName) {
        Optional<ResourceTemplate> resourceTemplate = repositoryHelper.findByTableName(tableName);
        resourceTemplate.ifPresent(template -> permissionChecker.checkReadPermission(template));
        return resourceTemplate;
    }

    public ResourceTemplate save(ResourceTemplate resourceTemplate) {
        return repositoryHelper.save(resourceTemplate);
    }

    public ResourceTemplate saveAndFlush(ResourceTemplate resourceTemplate) {
        return repositoryHelper.saveAndFlush(resourceTemplate);
    }

    public List<ResourceTemplate> findAllByIsPublishedIsTrue() {
        return repositoryHelper.findAllByIsPublishedIsTrue();
    }
}

@Repository
interface ResourceTemplateRepositoryHelper extends JpaRepository<ResourceTemplate, Long>,
        JpaSpecificationExecutor<ResourceTemplate> {

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
    @PreAuthorize("hasPermission(#id, 'com.softserve.rms.entities.ResourceTemplate', 'write') and hasRole('MANAGER')")
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
    @PostFilter("hasPermission(filterObject, 'read') or hasRole('MANAGER')")
    List<ResourceTemplate> findAllByUserId(Long id);

    /**
     * Method finds list of{@link ResourceTemplate} by name or description.
     *
     * @param name        searched word
     * @param description searched word
     * @return list of {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    @PostFilter("hasPermission(filterObject, 'read') or hasRole('MANAGER')")
    List<ResourceTemplate> findByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCase(String name, String description);

    /**
     * Method finds {@link Optional<ResourceTemplate>} by name.
     *
     * @param name of {@link ResourceTemplate}
     * @return {@link Optional<ResourceTemplate>}
     * @author Halyna Yatseniuk
     */
    @PreAuthorize("hasAnyRole({'USER', 'REGISTER', 'MANAGER'})")
    Optional<ResourceTemplate> findByName(String name);

    /**
     * Method finds {@link Optional<ResourceTemplate>} by name with case ignore.
     *
     * @param name of {@link ResourceTemplate}
     * @return {@link Optional<ResourceTemplate>}
     * @author Halyna Yatseniuk
     */
    @PreAuthorize("hasAnyRole({'USER', 'REGISTER', 'MANAGER'})")
    Optional<ResourceTemplate> findByNameIgnoreCase(String name);

    /**
     * Method finds {@link Optional<ResourceTemplate>} by table name.
     *
     * @param tableName of {@link ResourceTemplate}
     * @return {@link Optional<ResourceTemplate>}
     * @author Halyna Yatseniuk
     */
    @PreAuthorize("hasAnyRole({'USER', 'REGISTER', 'MANAGER'})")
    Optional<ResourceTemplate> findByTableName(String tableName);

    /**
     * Method creates a new {@link ResourceTemplate}.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @return created {@link ResourceTemplate}
     * @author Halyna Yatseniuk
     */
    @PreAuthorize("hasPermission(#resourceTemplate, 'write') and hasRole('MANAGER')")
    ResourceTemplate save(ResourceTemplate resourceTemplate);

    @PreAuthorize("hasRole('MANAGER')")
    ResourceTemplate saveAndFlush(ResourceTemplate resourceTemplate);

    @PostFilter("hasPermission(filterObject, 'read') or hasRole('MANAGER')")
    List<ResourceTemplate> findAllByIsPublishedIsTrue();
}