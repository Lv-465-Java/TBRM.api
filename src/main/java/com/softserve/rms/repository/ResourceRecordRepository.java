package com.softserve.rms.repository;

import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRecordRepository {

    /**
     * Method saves dynamic {@link ResourceRecord} in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param resourceRecord  instance of {@link ResourceRecord}
     * @author Andrii Bren
     */
    void save(String tableName, ResourceRecord resourceRecord);

    /**
     * Method updates dynamic {@link ResourceRecord} in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id        {@link ResourceRecord} id
     * @param resourceRecord  instance of {@link ResourceRecord}
     * @author Andrii Bren
     */
    void update(String tableName, Long id, ResourceRecord resourceRecord);

    /**
     * Method finds all dynamic {@link ResourceRecord} in a table specified
     * by the reсorder.
     *
     * @return list of dynamic {@link ResourceRecord}
     * @author Andrii Bren
     */
    List<ResourceRecord> findAll(String tableName);

    /**
     * Method finds dynamic {@link Optional< ResourceRecord >} by id.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @return {@link Optional< ResourceRecord >}
     * @throws NotFoundException if the resource with provided id is not found
     * @author Andrii Bren
     */
    Optional<ResourceRecord> findById(String tableName, Long id);

    /**
     * Method deletes dynamic {@link ResourceRecord} by id.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id        {@link ResourceRecord} id
     * @throws NotFoundException if the resource with provided id is not found
     * @author Andrii Bren
     */
    void delete(String tableName, Long id);
}
