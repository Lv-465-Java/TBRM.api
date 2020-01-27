package com.softserve.rms.service.implementation;


import com.softserve.rms.dto.ResourceParameterDTO;
import com.softserve.rms.dto.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.repository.ResourceParameterRepository;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.service.ResourceParameterService;
import com.softserve.rms.service.ResourceTemplateService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceParameterServiceImpl implements ResourceParameterService {
    private final ResourceParameterRepository resourceParameterRepository;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public ResourceParameterServiceImpl(ResourceParameterRepository resourceParameterRepository) {
        this.resourceParameterRepository = resourceParameterRepository;
    }


    @Override
    public ResourceParameterDTO create(ResourceParameterDTO resourceParameterDTO) {
        ResourceParameter resourceParameter = resourceParameterRepository
                .save(modelMapper.map(resourceParameterDTO, ResourceParameter.class));
        return modelMapper.map(resourceParameter, ResourceParameterDTO.class);
    }

    @Override
    public ResourceParameterDTO update(Long id, ResourceParameterDTO resourceParameterDTO) {
        ResourceParameter resourceParameter = getById(id);
        if (!resourceParameterDTO.getName().isEmpty()) {
            resourceParameter.setName(resourceParameterDTO.getName());
        }
        if (!resourceParameterDTO.getTypeName().isEmpty()) {
            resourceParameter.setTypeName(resourceParameterDTO.getTypeName());
        }
        if (resourceParameterDTO.getFieldType().isEmpty()) {
            resourceParameter.setFieldType(resourceParameterDTO.getFieldType());
        }
        if (resourceParameterDTO.getPattern().isEmpty()) {
            resourceParameter.setPattern(resourceParameterDTO.getPattern());
        }
        return modelMapper.map(resourceParameter, ResourceParameterDTO.class);
    }

    private ResourceParameter getById(Long id) {
        return resourceParameterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error"));
    }

    @Override
    public ResourceParameterDTO getOne(Long id) {
        return modelMapper.map(getById(id),
                ResourceParameterDTO.class);
    }

//    @Override
//    public List<ResourceParameterDTO> getAllByTemplateId(Long id) {
//        List<ResourceParameterDTO> parameters = resourceParameterRepository
//                .findAllByResourceTemplateId(id)
//                .stream()
//                .map(parameter -> modelMapper.map(parameter, ResourceParameterDTO.class))
//                .collect(Collectors.toList());
//        if (parameters.isEmpty()) {
//            throw new RuntimeException("Error");
//        }
//        return parameters;
//    }
//    @Override
//    public List<ResourceParameterDTO> getAllByTemplateId(Long id) {
//        List<ResourceParameterDTO> parameters = resourceParameterRepository
//                .findAllByResourceTemplateId(id)
//                .stream()
//                .map(ResourceParameterDTO::new)
//                .collect(Collectors.toList());
//        if (parameters.isEmpty()) {
//            throw new RuntimeException("Error");
//        }
//        return parameters;
//    }

    @Override
    public List<ResourceParameterDTO> getAllByTemplateId(Long id) {
        return modelMapper.map(resourceParameterRepository.findAllByResourceTemplateId(id),
                new TypeToken<List<ResourceParameterDTO>>() {
                }.getType());
    }

    @Override
    public void delete(Long id) {
        resourceParameterRepository.deleteById(id);
    }
}