package com.softserve.rms.service;

import com.softserve.rms.dto.resourceparameter.ResourceParameterDTO;

import java.util.List;


import com.softserve.rms.dto.resourceparameter.ResourceParameterSaveDTO;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.NotDeletedException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.exceptions.resourceParameter.ResourceParameterCanNotBeModified;

public interface ResourceParameterService {

    /**
     * Method saves {@link ResourceParameter}.
     *
     * @param templateId       {@link ResourceTemplate} id
     * @param parameterSaveDTO {@link ResourceParameterSaveDTO}
     * @return instance of {@link ResourceParameterDTO}
     * @throws NotUniqueNameException if the resource parameter with provided name exists
     * @throws NotFoundException      if the resource parameter with provided id is not found
     * @author Andrii Bren
     */
    ResourceParameterDTO save(Long templateId, ResourceParameterSaveDTO parameterSaveDTO);

    /**
     * Method finds one {@link ResourceParameter} by id.
     *
     * @param templateId  {@link ResourceTemplate} id
     * @param parameterId {@link ResourceParameterDTO} id
     * @return instance of {@link ResourceParameterDTO}
     * @throws NotFoundException if the resource parameter with provided id is not found
     * @author Andrii Bren
     */
    ResourceParameterDTO findByIdDTO(Long templateId, Long parameterId);

    /**
     * Method updates {@link ResourceParameter}.
     *
     * @param templateId       {@link ResourceTemplate} id
     * @param parameterId      {@link ResourceParameterDTO} id
     * @param parameterSaveDTO {@link ResourceParameterSaveDTO}
     * @return updated instance of {@link ResourceParameterDTO}
     * @throws NotUniqueNameException if the resource parameter with provided name exists
     * @throws NotFoundException      if the resource parameter with provided id is not found
     * @author Andrii Bren
     */
    ResourceParameterDTO updateById(Long templateId, Long parameterId, ResourceParameterSaveDTO parameterSaveDTO);

    /**
     * Method finds all {@link ResourceParameter} by {@link ResourceTemplate} id.
     *
     * @param templateId {@link ResourceTemplate} id
     * @return list of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    List<ResourceParameterDTO> findAllByTemplateId(Long templateId);

    /**
     * Method deletes {@link ResourceParameter} by id.
     *
     * @param templateId  {@link ResourceTemplate} id
     * @param parameterId {@link ResourceParameter} id
     * @throws NotDeletedException               if the resource parameter with provided id is not deleted
     * @throws ResourceParameterCanNotBeModified if the resource template is published
     * @author Halyna Yatseniuk
     */
    void delete(Long templateId, Long parameterId);
}
