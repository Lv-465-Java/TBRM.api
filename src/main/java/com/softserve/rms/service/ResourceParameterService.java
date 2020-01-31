package com.softserve.rms.service;

import com.softserve.rms.dto.resourceparameter.ResourceParameterDTO;

import java.util.List;


import com.softserve.rms.dto.resourceparameter.ResourceParameterSaveDTO;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceTemplate;

public interface ResourceParameterService {

    /**
     * Method saves {@link ResourceParameter}.
     *
     * @param parameterSaveDTO {@link ResourceParameterSaveDTO}
     * @return instance of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    ResourceParameterDTO save(ResourceParameterSaveDTO parameterSaveDTO);

    /**
     * Method finds one {@link ResourceParameter} by id.
     *
     * @param id {@link ResourceParameterDTO} id
     * @return instance of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    ResourceParameterDTO findByIdDTO(Long id);

    /**
     * Method updates {@link ResourceParameter}.
     *
     * @param id               {@link ResourceParameterDTO} id
     * @param parameterSaveDTO {@link ResourceParameterSaveDTO}
     * @return updated instance of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    ResourceParameterDTO update(Long id, ResourceParameterSaveDTO parameterSaveDTO);

    /**
     * Method finds all {@link ResourceParameter}.
     *
     * @return list of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    List<ResourceParameterDTO> findAll();

    /**
     * Method finds all {@link ResourceParameter} by {@link ResourceTemplate} id.
     *
     * @param id {@link ResourceTemplate} id
     * @return list of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    List<ResourceParameterDTO> findAllByTemplateId(Long id);

    /**
     * Method deletes {@link ResourceParameter} by id.
     *
     * @param id {@link ResourceParameter} id
     * @return id of deleted {@link ResourceParameter}
     * @author Andrii Bren
     */
    void delete(Long id);
}