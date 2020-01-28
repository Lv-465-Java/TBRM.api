package com.softserve.rms.service.implementation;


import com.softserve.rms.dto.ResourceParameterDTO;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.repository.ResourceParameterRepository;
import com.softserve.rms.service.ResourceParameterService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link ResourceParameterService}
 *
 * @author Andrii Bren
 */
@Service
public class ResourceParameterServiceImpl implements ResourceParameterService {
    private final ResourceParameterRepository resourceParameterRepository;
    private ModelMapper modelMapper = new ModelMapper();

    /**
     * Constructor with parameters
     *
     * @author Andrii Bren
     */
    @Autowired
    public ResourceParameterServiceImpl(ResourceParameterRepository resourceParameterRepository) {
        this.resourceParameterRepository = resourceParameterRepository;
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResourceParameterDTO save(ResourceParameterDTO resourceParameterDTO) {
        ResourceParameter resourceParameter = resourceParameterRepository
                .save(modelMapper.map(resourceParameterDTO, ResourceParameter.class));
        return modelMapper.map(resourceParameter, ResourceParameterDTO.class);
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
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

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public List<ResourceParameterDTO> getAll() {
        return modelMapper.map(resourceParameterRepository.findAll(),
                new TypeToken<List<ResourceParameterDTO>>() {
                }.getType());
    }

    /**
     * Method find {@link ResourceParameter} by id.
     *
     * @param id {@link ResourceParameter} id
     * @return instance of {@link ResourceParameter}
     * @author Andrii Bren
     */
    private ResourceParameter getById(Long id) {
        return resourceParameterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error"));
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResourceParameterDTO getByIdDTO(Long id) {
        return modelMapper.map(getById(id),
                ResourceParameterDTO.class);
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public List<ResourceParameterDTO> getAllByTemplateId(Long id) {
        return modelMapper.map(resourceParameterRepository.findAllByResourceTemplateId(id),
                new TypeToken<List<ResourceParameterDTO>>() {
                }.getType());
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public Long delete(Long id) {
        resourceParameterRepository.deleteById(id);
        return id;
    }
}