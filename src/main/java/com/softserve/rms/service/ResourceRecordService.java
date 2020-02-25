package com.softserve.rms.service;

import com.softserve.rms.dto.resourcerecord.ResourceRecordDTO;
import com.softserve.rms.dto.resourcerecord.ResourceRecordSaveDTO;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.NotFoundException;

import java.util.List;

public interface ResourceRecordService {

    /**
     * Method saves dynamic {@link ResourceRecordSaveDTO} in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param resource  instance of {@link ResourceRecordSaveDTO}
     * @throws NotFoundException if the resource with provided id or name is not found
     * @author Andrii Bren
     */
    void save(String tableName, ResourceRecordSaveDTO resource);


    /**
     * Method finds dynamic {@link ResourceRecordDTO} in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id {@link ResourceRecordDTO} id
     * @return {@link ResourceRecordDTO}
     * @throws NotFoundException if the resource with provided id or name is not found
     * @author Andrii Bren
     */
    ResourceRecordDTO findByIdDTO(String tableName, Long id);

    /**
     * Method finds dynamic {@link ResourceRecord} by provided id in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id of {@link ResourceRecordDTO}
     * @return {@link ResourceRecord}
     * @throws NotFoundException if the resource with provided id is not found
     * @author Andrii Bren
     */
    ResourceRecord findById(String tableName, Long id);

    /**
     * Method finds all dynamic {@link ResourceRecordDTO} in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @return list of dynamic {@link ResourceRecordDTO}
     * @throws NotFoundException if the resource with provided id or name is not found
     * @author Andrii Bren
     */
    List<ResourceRecordDTO> findAll(String tableName);

    /**
     * Method updates dynamic {@link ResourceRecordDTO} in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id        {@link ResourceRecord} id
     * @param resourceRecordSaveDTO instance of {@link ResourceRecordSaveDTO}
     * @throws NotFoundException if the resource with provided id or name is not found
     * @author Andrii Bren
     */
    void update(String tableName, Long id, ResourceRecordSaveDTO resourceRecordSaveDTO);

    /**
     * Method deletes dynamic {@link ResourceRecordDTO} by id.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id {@link ResourceRecord} id
     * @throws NotFoundException if the resource with provided id or name is not found
     * @author Andrii Bren
     */
    void delete(String tableName, Long id);
}
