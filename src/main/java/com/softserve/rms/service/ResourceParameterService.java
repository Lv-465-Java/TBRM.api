package com.softserve.rms.service;

import com.softserve.rms.dto.ResourceParameterDTO;

import java.util.List;

public interface ResourceParameterService {

    ResourceParameterDTO save(ResourceParameterDTO resourceParameterDTO);

    ResourceParameterDTO getByIdDTO(Long id);

    ResourceParameterDTO update(Long id, ResourceParameterDTO resourceParameterDTO);

    List<ResourceParameterDTO> getAll();

    List<ResourceParameterDTO> getAllByTemplateId(Long id);

    void delete(Long id);



}
