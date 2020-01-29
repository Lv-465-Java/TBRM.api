package com.softserve.rms.service.implementation;


import com.softserve.rms.dto.ResourceParameterDTO;
import com.softserve.rms.dto.ResourceRelationDTO;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceRelation;
import com.softserve.rms.repository.ResourceParameterRepository;
import com.softserve.rms.repository.ResourceRelationRepository;
import com.softserve.rms.service.ResourceParameterService;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.validator.RangeIntegerPatternGenerator;
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
    private final ResourceRelationRepository resourceRelationRepository;
    private final ResourceTemplateService resourceTemplateService;

    private ModelMapper modelMapper = new ModelMapper();
    private RangeIntegerPatternGenerator patternGenerator = new RangeIntegerPatternGenerator();

    /**
     * Constructor with parameters
     *
     * @author Andrii Bren
     */
    @Autowired
    public ResourceParameterServiceImpl(ResourceParameterRepository resourceParameterRepository,
                                        ResourceTemplateService resourceTemplateService,
                                        ResourceRelationRepository resourceRelationRepository) {
        this.resourceParameterRepository = resourceParameterRepository;
        this.resourceTemplateService = resourceTemplateService;
        this.resourceRelationRepository = resourceRelationRepository;
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResourceParameterDTO save(ResourceParameterDTO parameterDTO) {
        ResourceParameter resourceParameter = resourceParameterRepository
                .save(getResourceParameter(null, parameterDTO));
        if (parameterDTO.getResourceRelations() != null) {
            saveRelations(resourceParameter.getId(), parameterDTO.getResourceRelations());
        }
        return modelMapper.map(resourceParameter, ResourceParameterDTO.class);
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResourceParameterDTO update(Long id, ResourceParameterDTO parameterDTO) {
        ResourceParameter resourceParameter = resourceParameterRepository
                .save(getResourceParameter(findById(id), parameterDTO));
        if (parameterDTO.getResourceRelations() != null) {
            saveRelations(resourceParameter.getId(), parameterDTO.getResourceRelations());
        }
        return modelMapper.map(resourceParameter, ResourceParameterDTO.class);
    }

    public void saveRelations(Long parameterId, List<ResourceRelationDTO> list) {
        for (ResourceRelationDTO dto : list) {
            ResourceRelation resourceRelation = new ResourceRelation();
            resourceRelation.setResourceParameter(findById(parameterId));
            resourceRelation.setRelatedResourceTemplate(resourceTemplateService
                    .findById(dto.getRelatedResourceTemplateId()));
            resourceRelationRepository.save(resourceRelation);
        }
    }

    private ResourceParameter getResourceParameter(ResourceParameter resourceParameter, ResourceParameterDTO parameterDTO) {
        if (resourceParameter == null) {
            resourceParameter = new ResourceParameter();
        }
        resourceParameter.setColumnName(parameterDTO.getColumnName());
        resourceParameter.setParameterType(parameterDTO.getParameterType());
        resourceParameter.setPattern(patternGenerator.generateRangeIntegerRegex(parameterDTO.getPattern()));
//        resourceParameter.setPattern(parameterDTO.getPattern());
        resourceParameter.setResourceTemplate(resourceTemplateService.findById(parameterDTO.getResourceTemplateId()));
        return resourceParameter;
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
    public ResourceParameter findById(Long id) {
        return resourceParameterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error"));
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResourceParameterDTO findByIdDTO(Long id) {
        return modelMapper.map(findById(id),
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