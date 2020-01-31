package com.softserve.rms.service.implementation;


import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.resourceparameter.ResourceParameterDTO;
import com.softserve.rms.dto.resourceparameter.ResourceParameterSaveDTO;
import com.softserve.rms.dto.resourceparameter.ResourceRelationDTO;
import com.softserve.rms.entities.ParameterType;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceRelation;
import com.softserve.rms.exceptions.NotDeletedException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.repository.ResourceParameterRepository;
import com.softserve.rms.repository.ResourceRelationRepository;
import com.softserve.rms.service.ResourceParameterService;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.util.RangeIntegerPatternGenerator;
import com.softserve.rms.util.Validator;
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
    private Validator validator = new Validator();

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
    public ResourceParameterDTO save(ResourceParameterSaveDTO parameterDTO)
            throws NotFoundException, NotUniqueNameException {
        ResourceParameter resourceParameter = new ResourceParameter();
        resourceParameter.setName(getValidName(parameterDTO.getName()));
        resourceParameter.setColumnName(validator.generateTableOrColumnName(parameterDTO.getName()));
        resourceParameter.setParameterType(parameterDTO.getParameterType());
        if (parameterDTO.getPattern() != null ||
                parameterDTO.getParameterType() == ParameterType.AREA_DOUBLE) {
            resourceParameter.setPattern(getMatchedPatternToParameterType(
                    parameterDTO.getParameterType(), parameterDTO.getPattern()));
        }
        resourceParameter.setResourceTemplate(
                resourceTemplateService.findById(parameterDTO.getResourceTemplateId()));

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
            return Validator.COORDINATES_PATTERN;
        } else if (type == ParameterType.POINT_STRING) {
            //TODO
            return "pattern for string";
        }
        //TODO
        return "pattern for double";
    }

    /**
     * Method checks whether parameter name is valid.
     *
     * @param parameterName String {@link ResourceParameter} name
     * @return String validName
     * @throws NotUniqueNameException if the resource parameter with provided name exists
     * @author Andrii Bren
     */
    private String getValidName(String parameterName) throws NotUniqueNameException {
        if (resourceParameterRepository.findByName(parameterName).isPresent()) {
            throw new NotUniqueNameException(
                    ErrorMessage.RESOURCE_PARAMETER_IS_ALREADY_EXISTED.getMessage() + parameterName);
        }
        return parameterName;
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    @Transactional
    public ResourceParameterDTO update(Long id, ResourceParameterSaveDTO parameterDTO)
            throws NotFoundException, NotUniqueNameException {
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
     * @throws NotFoundException if the resource template or parameter with provided id is not found
     * @author Andrii Bren
     */
    private ResourceRelation saveRelation(Long parameterId, ResourceRelationDTO relationDTO)
            throws NotFoundException {
        ResourceRelation resourceRelation = new ResourceRelation();
        resourceRelation.setResourceParameter(findById(parameterId));
        resourceRelation.setRelatedResourceTemplate(resourceTemplateService
                .findById(relationDTO.getRelatedResourceTemplateId()));
        return resourceRelationRepository.save(resourceRelation);
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
     * @throws NotFoundException if the resource parameter with provided id is not found
     * @author Andrii Bren
     */
    public ResourceParameter findById(Long id) throws NotFoundException {
        return resourceParameterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorMessage.RESOURCE_PARAMETER_CAN_NOT_BE_FOUND_BY_ID.getMessage() + id));
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResourceParameterDTO findByIdDTO(Long id) throws NotFoundException {
        return modelMapper.map(findById(id),
                ResourceParameterDTO.class);
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public List<ResourceParameterDTO> findAllByTemplateId(Long id) throws NotFoundException {
        List<ResourceParameter> parameterList = resourceParameterRepository
                .findAllByResourceTemplateId(
                        resourceTemplateService.findById(id).getId());
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
    public void delete(Long id) throws NotDeletedException {
        try {
            resourceParameterRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotDeletedException(
                    ErrorMessage.RESOURCE_PARAMETER_CAN_NOT_BE_DELETE_BY_ID.getMessage() + id);
        }
    }

}