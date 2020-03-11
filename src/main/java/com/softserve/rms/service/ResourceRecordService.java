package com.softserve.rms.service;

import com.softserve.rms.dto.resourceRecord.ResourceRecordDTO;
import com.softserve.rms.dto.resourceRecord.ResourceRecordSaveDTO;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.NotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    /**
     * Method that allow you to save photo
     *
     * @param files for saving
     * @param tableName {@link ResourceTemplate} tableName
     * @param id {@link ResourceRecord} id
     * @author Mariia Shchur
     */
    void changePhoto(MultipartFile files, String tableName, Long id);

    /**
     * Method that allow you to delete specific photo
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id {@link ResourceRecord} id
     * @param photo
     * @author Mariia Shchur
     */
    void deletePhoto(String tableName, Long id,String photo);

    /**
     * Method that allow you to delete all {@link ResourceRecord}'s photos
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id {@link ResourceRecord} id
     * @author Mariia Shchur
     */
    void deleteAllPhotos(String tableName, Long id);

    /**
     * Method that allow you to upload documents
     *
     * @param files for saving
     * @param tableName {@link ResourceTemplate} tableName
     * @param id {@link ResourceRecord} id
     * @author Mariia Shchur
     */
    void uploadDocument(MultipartFile files, String tableName, Long id);

    /**
     * Method that allow you to delete all {@link ResourceRecord}'s documents
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id {@link ResourceRecord} id
     * @author Mariia Shchur
     */
    void deleteAllDocuments(String tableName, Long id);

    /**
     * Method that allow you to delete specific document
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id {@link ResourceRecord} id
     * @param document
     * @author Mariia Shchur
     */
    void deleteDocument(String tableName, Long id, String document);
}
