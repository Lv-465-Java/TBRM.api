package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.dto.resourceRecord.ResourceRecordDTO;
import com.softserve.rms.dto.resourceRecord.ResourceRecordSaveDTO;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.User;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateIsNotPublishedException;
import com.softserve.rms.repository.ResourceRecordRepository;
import com.softserve.rms.service.ResourceRecordService;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ResourceRecordService}
 *
 * @author Andrii Bren
 */
@Service
public class ResourceRecordServiceImpl implements ResourceRecordService {
    private ResourceRecordRepository resourceRecordRepository;
    private ResourceTemplateService resourceTemplateService;
    private UserService userService;
    private ModelMapper modelMapper = new ModelMapper();

    /**
     * Constructor with parameters
     *
     * @author Andrii Bren
     */
    @Autowired
    public ResourceRecordServiceImpl(ResourceRecordRepository resourceRecordRepository, ResourceTemplateService resourceTemplateService, UserService userService) {
        this.resourceRecordRepository = resourceRecordRepository;
        this.resourceTemplateService = resourceTemplateService;
        this.userService = userService;
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public void save(String tableName, ResourceRecordSaveDTO resourceDTO) throws NotFoundException {
        checkIfResourceTemplateIsPublished(tableName);
        ResourceRecord resourceRecord = new ResourceRecord();
        resourceRecord.setName(resourceDTO.getName());
        resourceRecord.setDescription(resourceDTO.getDescription());
//        resourceRecord.setResourceTemplate(resourceTemplate);
        User user = userService.getById(resourceDTO.getUserId());
        resourceRecord.setUser(user);
        resourceRecord.setParameters(resourceDTO.getParameters());
        resourceRecordRepository.save(tableName, resourceRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public ResourceRecordDTO findByIdDTO(String tableName, Long id) throws NotFoundException {
        return modelMapper.map(findById(tableName, id), ResourceRecordDTO.class);
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    public ResourceRecord findById(String tableName, Long id) throws NotFoundException {
        checkIfResourceTemplateIsPublished(tableName);
        return resourceRecordRepository.findById(tableName, id)
                .orElseThrow(() -> new NotFoundException(
                        ErrorMessage.CAN_NOT_FIND_A_RESOURCE_TABLE.getMessage() + id));
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public List<ResourceRecordDTO> findAll(String tableName) throws NotFoundException {
        checkIfResourceTemplateIsPublished(tableName);
        List<ResourceRecord> resourceRecords = resourceRecordRepository.findAll(tableName);
        return resourceRecords.stream()
                .map(resource -> modelMapper.map(resource, ResourceRecordDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public void update(String tableName, Long id, ResourceRecordSaveDTO resourceRecordSaveDTO)
            throws NotFoundException {
        checkIfResourceTemplateIsPublished(tableName);
        ResourceRecord resourceRecord = findById(tableName, id);
        if (resourceRecordSaveDTO.getName() != null) {
            resourceRecord.setName(resourceRecordSaveDTO.getName());
        }
        if (resourceRecordSaveDTO.getDescription() != null) {
            resourceRecord.setDescription(resourceRecordSaveDTO.getDescription());
        }
        if (resourceRecordSaveDTO.getParameters() != null) {
            resourceRecord.setParameters(resourceRecordSaveDTO.getParameters());
        }
        resourceRecordRepository.update(tableName, id, resourceRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Override
    public void delete(String tableName, Long id) throws NotFoundException {
        checkIfResourceTemplateIsPublished(tableName);
        resourceRecordRepository.delete(tableName, id);
    }

    private void checkIfResourceTemplateIsPublished(String tableName) {
        ResourceTemplate resourceTemplate = resourceTemplateService.findByName(tableName);
        if (!resourceTemplate.getIsPublished()) {
            throw new ResourceTemplateIsNotPublishedException(
                    ErrorMessage.RESOURCE_TEMPLATE_IS_NOT_PUBLISHED.getMessage() + tableName);
        }
//        return resourceTemplate;
    }
}
