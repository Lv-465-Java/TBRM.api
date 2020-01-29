package com.softserve.rms.service;

import com.softserve.rms.dto.ResourceParameterDTO;

import java.util.List;


import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceTemplate;

public interface ResourceParameterService {

    /**
     * Method saves {@link ResourceParameter}.
     *
     * @param resourceParameterDTO {@link ResourceParameterDTO}
     * @return instance of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    ResourceParameterDTO save(ResourceParameterDTO resourceParameterDTO);

    /**
     * Method finds one {@link ResourceParameter} by id.
     *
     * @param id {@link ResourceParameterDTO} id
     * @return instance of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    ResourceParameterDTO getByIdDTO(Long id);

    /**
     * Method updates {@link ResourceParameter}.
     *
     * @param id {@link ResourceParameterDTO} id
     * @param resourceParameterDTO {@link ResourceParameterDTO}
     * @return updated instance of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    ResourceParameterDTO update(Long id, ResourceParameterDTO resourceParameterDTO);

    /**
     * Method finds all {@link ResourceParameter}.
     *
     * @return list of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    List<ResourceParameterDTO> getAll();

    /**
     * Method finds all {@link ResourceParameter} by {@link ResourceTemplate} id.
     *
     * @param id {@link ResourceTemplate} id
     * @return list of {@link ResourceParameterDTO}
     * @author Andrii Bren
     */
    List<ResourceParameterDTO> getAllByTemplateId(Long id);

    /**
     * Method deletes {@link ResourceParameter} by id.
     *
     * @param id {@link ResourceParameter} id
     * @return id of deleted {@link ResourceParameter}
     * @author Andrii Bren
     */
    Long delete(Long id);




}
