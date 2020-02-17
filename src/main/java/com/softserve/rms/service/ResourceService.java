package com.softserve.rms.service;

import com.softserve.rms.dto.resource.ResourceDTO;
import com.softserve.rms.dto.resource.ResourceSaveDTO;
import com.softserve.rms.entities.Resource;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ResourceService {

    /**
     * Method saves dynamic {@link ResourceSaveDTO} in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param resource  instance of {@link ResourceSaveDTO}
     * @throws NotFoundException if the resource with provided id or name is not found
     * @author Andrii Bren
     */
    void save(String tableName, ResourceSaveDTO resource);


    /**
     * Method finds dynamic {@link ResourceDTO} in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id {@link Resource} id
     * @return {@link ResourceDTO}
     * @throws NotFoundException if the resource with provided id or name is not found
     * @author Andrii Bren
     */
    ResourceDTO findByIdDTO(String tableName, Long id);

    /**
     * Method finds all dynamic {@link ResourceDTO} in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @return list of dynamic {@link ResourceDTO}
     * @throws NotFoundException if the resource with provided id or name is not found
     * @author Andrii Bren
     */
    List<ResourceDTO> findAll(String tableName);

    /**
     * Method updates dynamic {@link ResourceDTO} in a table specified
     * by the reсorder.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id        {@link Resource} id
     * @param body      map with {@link ResourceParameter} where key is the name
     *                  of parameter and value is the value of parameter.
     * @throws NotFoundException if the resource with provided id or name is not found
     * @author Andrii Bren
     */
    void update(String tableName, Long id, Map<String, HashMap<String, Object>> body);

    /**
     * Method deletes dynamic {@link ResourceDTO} by id.
     *
     * @param tableName {@link ResourceTemplate} tableName
     * @param id {@link Resource} id
     * @throws NotFoundException if the resource with provided id or name is not found
     * @author Andrii Bren
     */
    void delete(String tableName, Long id);
}
