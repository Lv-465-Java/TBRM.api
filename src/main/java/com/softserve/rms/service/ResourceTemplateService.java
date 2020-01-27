package com.softserve.rms.service;

import com.softserve.rms.dto.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.User;

import java.util.List;

public interface ResourceTemplateService {
    /**
     * Method creates {@link ResourceTemplate}.
     *
     * @param resourceTemplateDTO {@link ResourceTemplateDTO}
     * @return new {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    ResourceTemplateDTO createTemplate(ResourceTemplateDTO resourceTemplateDTO);

    /**
     * Method finds {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    ResourceTemplateDTO getTemplateById(Long id);

    /**
     * Method finds all {@link ResourceTemplate} by user id.
     *
     * @param id of {@link User}
     * @return List of all {@link ResourceTemplateDTO} for user
     * @author Halyna Yatseniuk
     */
    List<ResourceTemplateDTO> getAllByUserId(Long id);

    /**
     * Method updates {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    ResourceTemplateDTO updateTemplateById(Long id, ResourceTemplateDTO resourceTemplateDTO);

    /**
     * Method deletes {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    void deleteTemplateById(Long id);

    /**
     * Method finds all {@link ResourceTemplate} by name or description.
     *
     * @param name of {@link ResourceTemplateDTO}
     * @param description of {@link ResourceTemplateDTO}
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    List<ResourceTemplateDTO> searchTemplateByNameOrDescriptionContaining(String name, String description);


}