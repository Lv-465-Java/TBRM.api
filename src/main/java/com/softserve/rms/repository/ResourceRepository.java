package com.softserve.rms.repository;

import com.softserve.rms.entities.Resource;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository {

    /**
     * Method saves dynamic {@link Resource} in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param resource  instance of {@link Resource}
     * @author Andrii Bren
     */
    void save(String tableName, Resource resource);

    /**
     * Method updates dynamic {@link Resource} in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id        {@link Resource} id
     * @param resource  instance of {@link Resource}
     * @author Andrii Bren
     */
    void update(String tableName, Long id, Resource resource);

    /**
     * Method finds all dynamic {@link Resource} in a table specified
     * by the reсorder.
     *
     * @return list of dynamic {@link Resource}
     * @author Andrii Bren
     */
    List<Resource> findAll(String tableName);

    /**
     * Method finds dynamic {@link Optional<Resource>} by id.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @return {@link Optional<Resource>}
     * @throws NotFoundException if the resource with provided id is not found
     * @author Andrii Bren
     */
    Optional<Resource> findById(String tableName, Long id);

    /**
     * Method deletes dynamic {@link Resource} by id.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id        {@link Resource} id
     * @throws NotFoundException if the resource with provided id is not found
     * @author Andrii Bren
     */
    void delete(String tableName, Long id);
}
