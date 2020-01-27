package com.softserve.rms.service;

import com.softserve.rms.dto.ResourceParameterDTO;
import com.softserve.rms.entities.ResourceParameter;

import java.util.List;

public interface ResourceParameterService {

    ResourceParameterDTO create(ResourceParameterDTO resourceParameterDTO);

    ResourceParameterDTO getOne(Long id);

    ResourceParameterDTO update(Long id, ResourceParameterDTO resourceParameterDTO);

    List<ResourceParameterDTO> getAllByTemplateId(Long id);


    void delete(Long id);



}
