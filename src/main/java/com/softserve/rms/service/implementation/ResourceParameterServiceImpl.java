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
import com.softserve.rms.exceptions.NotDeletedException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.NotUniqueNameException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateIsNotPublishedException;

//import static com.softserve.rms.jooq.tables.ResourceParameters.RESOURCE_PARAMETERS;
//import com.softserve.rms.jooq.tables.records.ResourceParametersRecord;

import com.softserve.rms.jooq.tables.ResourceParameters;
import com.softserve.rms.repository.ResourceParameterRepository;
import com.softserve.rms.repository.ResourceRelationRepository;
import com.softserve.rms.service.ResourceParameterService;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.util.RangeIntegerPatternGenerator;
import com.softserve.rms.util.Validator;
import org.jooq.DSLContext;
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

    ResourceParameters resourceParameters = ResourceParameters.RESOURCE_PARAMETERS;


    private ModelMapper modelMapper = new ModelMapper();
    private RangeIntegerPatternGenerator patternGenerator = new RangeIntegerPatternGenerator();


    private DSLContext dslContext;

    /**
     * Constructor with parameters
     *
     * @author Andrii Bren
     */
    @Autowired
    public ResourceParameterServiceImpl(ResourceParameterRepository resourceParameterRepository,
                                        ResourceTemplateService resourceTemplateService,
                                        ResourceRelationRepository resourceRelationRepository, DSLContext dslContext) {
        this.resourceParameterRepository = resourceParameterRepository;
        this.resourceTemplateService = resourceTemplateService;
        this.resourceRelationRepository = resourceRelationRepository;
        this.dslContext = dslContext;
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    @Transactional
    public ResourceParameterDTO save(Long id, ResourceParameterSaveDTO parameterDTO)
            throws NotFoundException, NotUniqueNameException {

        ResourceParameter resourceParameter = new ResourceParameter();
        resourceParameter.setName(parameterDTO.getName());
        resourceParameter.setColumnName(validator.generateTableOrColumnName(parameterDTO.getName()));
        resourceParameter.setParameterType(parameterDTO.getParameterType());
        if (parameterDTO.getPattern() != null ||
                parameterDTO.getParameterType() == ParameterType.AREA_DOUBLE) {
            resourceParameter.setPattern(getMatchedPatternToParameterType(
                    parameterDTO.getParameterType(), parameterDTO.getPattern()));
        }
        resourceParameter.setResourceTemplate(
                resourceTemplateService.findEntityById(id));

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
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    @Transactional
    public ResourceParameterDTO update(Long id, Long parameterId, ResourceParameterSaveDTO parameterDTO)
            throws NotFoundException, NotUniqueNameException {

        resourceTemplateService.findEntityById(id);

        ResourceParameter resourceParameter = findById(parameterId);
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
     * @throws NotFoundException                       if the resource template or parameter with provided id is not found
     * @throws ResourceTemplateIsNotPublishedException if resource template has been already published
     * @author Andrii Bren
     */
    private ResourceRelation saveRelation(Long parameterId, ResourceRelationDTO relationDTO)
            throws NotFoundException, ResourceTemplateIsNotPublishedException {
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
     * @throws ResourceTemplateIsNotPublishedException if resource template has been already published
     * @author Halyna Yatseniuk
     */
    private ResourceTemplate verifyIfResourceTemplateIsPublished(ResourceTemplate resourceTemplate)
            throws ResourceTemplateIsNotPublishedException {
        if (!resourceTemplate.getIsPublished()) {
            throw new ResourceTemplateIsNotPublishedException(ErrorMessage.RESOURCE_TEMPLATE_IS_NOT_PUBLISHED.getMessage());
        }
        return resourceTemplate;
    }

//    /**
//     * {@inheritDoc}
//     *
//     * @author Andrii Bren
//     */
//    @Override
//    public List<ResourceParameterDTO> findAll() {
//        return modelMapper.map(resourceParameterRepository.findAll(),
//                new TypeToken<List<ResourceParameterDTO>>() {
//                }.getType());
//    }

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
    public ResourceParameterDTO findByIdDTO(Long id, Long parameterId) throws NotFoundException {
        return modelMapper.map(findById(parameterId),
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
                        resourceTemplateService.findEntityById(id).getId());
        return modelMapper.map(parameterList,
                new TypeToken<List<ResourceParameterDTO>>() {
                }.getType());
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
//    @Override
//    @Transactional
//    public void delete(Long id, Long parameterId) throws NotDeletedException {
//        dslContext.delete(resourceParameters).where(resourceParameters.ID.eq(parameterId)).execute();
//    }
    @Override
    @Transactional
    public void delete(Long id, Long parameterId) throws NotDeletedException {
        resourceTemplateService.findEntityById(id);
        try {
            resourceParameterRepository.deleteById(parameterId);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotDeletedException(
                    ErrorMessage.RESOURCE_PARAMETER_CAN_NOT_BE_DELETE_BY_ID.getMessage() + parameterId);
        }
    }


}
