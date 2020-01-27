package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.User;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.service.ResourceTemplateService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link ResourceTemplateService}.
 *
 * @author Halyna Yatseniuk
 */
@Service
public class ResourceTemplateServiceImpl implements ResourceTemplateService {
    private final ResourceTemplateRepository resourceTemplateRepository;
    private ModelMapper modelMapper = new ModelMapper();

    /**
     * Constructor with parameters.
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public ResourceTemplateServiceImpl(ResourceTemplateRepository resourceTemplateRepository) {
        this.resourceTemplateRepository = resourceTemplateRepository;
    }

    /**
     * Method creates {@link ResourceTemplate}.
     *
     * @param resourceTemplateDTO {@link ResourceTemplateDTO}
     * @return new {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO createTemplate(ResourceTemplateDTO resourceTemplateDTO) {
        ResourceTemplate resourceTemplate = resourceTemplateRepository
                .save(modelMapper.map(resourceTemplateDTO, ResourceTemplate.class));
        return modelMapper.map(resourceTemplate, ResourceTemplateDTO.class);
    }

    /**
     * Method finds {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO getTemplateById(Long id) {
        return modelMapper.map(resourceTemplateRepository.getOne(id), ResourceTemplateDTO.class);
    }

    /**
     * Method finds all {@link ResourceTemplate} by user id.
     *
     * @param id of {@link User}
     * @return List of all {@link ResourceTemplateDTO} for user
     * @author Halyna Yatseniuk
     */
    @Override
    public List<ResourceTemplateDTO> getAllByUserId(Long id) {
        return modelMapper.map(resourceTemplateRepository.findAllByUserId(id), new TypeToken<List<ResourceTemplateDTO>>() {
        }.getType());
    }

    /**
     * Method updates {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @Override
    public ResourceTemplateDTO updateTemplateById(Long id, ResourceTemplateDTO resourceTemplateDTO) {
        ResourceTemplate resourceTemplate = resourceTemplateRepository.
                save(modelMapper.map(resourceTemplateDTO, ResourceTemplate.class));
        return modelMapper.map(resourceTemplate, ResourceTemplateDTO.class);
    }

    /**
     * Method deletes {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @Override
    public void deleteTemplateById(Long id) {
        resourceTemplateRepository.deleteById(id);
    }

    /**
     * Method finds all {@link ResourceTemplate} by name or description.
     *
     * @param name of {@link ResourceTemplateDTO}
     * @param description of {@link ResourceTemplateDTO}
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    @Override
    public List<ResourceTemplateDTO> searchTemplateByNameOrDescriptionContaining(String name, String description) {
        return null;
    }

//    public ResourceTemplate getById(Long id) {
//        return resourceTemplateRepository.findById(id)
//                .orElseThrow(()-> new RuntimeException("Error"));
//    }
}