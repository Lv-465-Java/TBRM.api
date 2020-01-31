package com.softserve.rms.service.implementation;


import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.resourceparameter.ResourceParameterDTO;
import com.softserve.rms.dto.resourceparameter.ResourceParameterSaveDTO;
import com.softserve.rms.dto.resourceparameter.ResourceRelationDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ParameterType;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceRelation;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.resourceparameter.ResourceParameterNotDeletedException;
import com.softserve.rms.exceptions.resourceparameter.ResourceParameterNotFoundException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateIsPublishedException;
import com.softserve.rms.repository.ResourceParameterRepository;
import com.softserve.rms.repository.ResourceRelationRepository;
import com.softserve.rms.service.ResourceParameterService;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.validator.RangeIntegerPatternGenerator;
import com.softserve.rms.validator.ResourceTemplateAndParameterValidator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ResourceTemplateAndParameterValidator validator = new ResourceTemplateAndParameterValidator();

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
    @Transactional
    public ResourceParameterDTO save(ResourceParameterSaveDTO parameterDTO) {
        ResourceParameter resourceParameter = new ResourceParameter();
        resourceParameter.setName(parameterDTO.getName());
        resourceParameter.setColumnName(validator.generateTableOrColumnName(parameterDTO.getName()));
        resourceParameter.setParameterType(parameterDTO.getParameterType());
        if (parameterDTO.getPattern() != null) {
            resourceParameter.setPattern(getMatchedPatternToParameterType(
                    parameterDTO.getParameterType(), parameterDTO.getPattern()));
        }
        resourceParameter.setResourceTemplate(
                resourceTemplateService.findEntityById(parameterDTO.getResourceTemplateId()));

        resourceParameterRepository.save(resourceParameter);

        if (parameterDTO.getResourceRelationDTO() != null) {
            resourceParameter.setResourceRelations(saveRelation(resourceParameter.getId(), parameterDTO.getResourceRelationDTO()));
        }
        return modelMapper.map(resourceParameter, ResourceParameterDTO.class);
    }

    /**
     * Method matches pattern to {@link ParameterType}.
     *
     * @param type    {@link ParameterType}
     * @param pattern regex pattern
     * @return String regex
     * @author Andrii Bren
     */
    private String getMatchedPatternToParameterType(ParameterType type, String pattern) {
        if (type == ParameterType.POINT_INT || type == ParameterType.RANGE_INT) {
            return patternGenerator.generateRangeIntegerRegex(pattern);
        } else if (type == ParameterType.AREA_DOUBLE) {
            return "pattern for coordinates";
        }
        //ToDo
        return "pattern for double";
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    @Transactional
    public ResourceParameterDTO update(Long id, ResourceParameterSaveDTO parameterDTO) {
        ResourceParameter resourceParameter = findById(id);
        resourceParameter.setColumnName(validator.generateTableOrColumnName(parameterDTO.getName()));
        resourceParameter.setParameterType(parameterDTO.getParameterType());
        if (parameterDTO.getPattern() != null) {
            resourceParameter.setPattern(getMatchedPatternToParameterType(
                    parameterDTO.getParameterType(), parameterDTO.getPattern()));
        }
        if (parameterDTO.getResourceRelationDTO() != null) {
            resourceParameterRepository.save(resourceParameter);
            resourceParameter.setResourceRelations(saveRelation(resourceParameter.getId(), parameterDTO.getResourceRelationDTO()));
        }
        return modelMapper.map(resourceParameter, ResourceParameterDTO.class);
    }

    /**
     * Method saves {@link ResourceRelation}.
     *
     * @param parameterId {@link ResourceParameter} id
     * @param relationDTO {@link ResourceRelationDTO}
     * @return instance of {@link ResourceRelation}
     */
    private ResourceRelation saveRelation(Long parameterId, ResourceRelationDTO relationDTO) {
        ResourceRelation resourceRelation = new ResourceRelation();
        resourceRelation.setResourceParameter(findById(parameterId));
        resourceRelation.setRelatedResourceTemplate(verifyIfResourceTemplateIsPublished(resourceTemplateService
                .findEntityById(relationDTO.getRelatedResourceTemplateId())));
        return resourceRelationRepository.save(resourceRelation);
    }

    /**
     * Method verifies if {@link ResourceTemplate} has been published.
     *
     * @param resourceTemplate {@link ResourceTemplate}
     * @return {@link ResourceTemplateDTO} if provided resource template hasn't been published yet
     * @throws ResourceTemplateIsPublishedException if resource template has been already published
     * @author Halyna Yatseniuk
     */
    private ResourceTemplate verifyIfResourceTemplateIsPublished(ResourceTemplate resourceTemplate) {
        if (!resourceTemplate.getIsPublished()) {
            throw new RuntimeException(ErrorMessage.RESOURCE_TEMPLATE_IS_NOT_PUBLISHED.getMessage());
        }
        return resourceTemplate;
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public List<ResourceParameterDTO> findAll() {
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
                .orElseThrow(() -> new ResourceParameterNotFoundException(
                        ErrorMessage.RESOURCE_PARAMETER_CAN_NOT_FOUND_BY_ID.getMessage() + id));
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
    public List<ResourceParameterDTO> findAllByTemplateId(Long id) {
        List<ResourceParameter> parameterList = resourceParameterRepository
                .findAllByResourceTemplateId(id)
                .orElseThrow(() -> new ResourceParameterNotFoundException(
                        ErrorMessage.RESOURCE_TEMPLATE_HAS_NOT_ANY_PARAMETERS.getMessage() + id));
        return modelMapper.map(parameterList,
                new TypeToken<List<ResourceParameterDTO>>() {
                }.getType());
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    @Transactional
    public void delete(Long id) {
        try {
            resourceParameterRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResourceParameterNotDeletedException(
                    ErrorMessage.RESOURCE_PARAMETER_CAN_NOT_DELETE_BY_ID.getMessage() + id);
        }
    }

}