package com.softserve.rms.service;

import com.softserve.rms.dto.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.Person;

import java.util.List;
import java.util.Map;

public interface ResourceTemplateService {
    /**
     * Method creates {@link ResourceTemplate}.
     *
     * @param resourceTemplateDTO {@link ResourceTemplateDTO}
     * @return new {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    ResourceTemplateDTO save(ResourceTemplateDTO resourceTemplateDTO);

    /**
     * Method finds {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    ResourceTemplateDTO getById(Long id);

    /**
     * Method finds all {@link ResourceTemplate} by user id.
     *
     * @param id of {@link Person}
     * @return List of all {@link ResourceTemplateDTO} for user
     * @author Halyna Yatseniuk
     */
    List<ResourceTemplateDTO> getAllByPersonId(Long id);

    /**
     * Method updates {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    ResourceTemplateDTO updateById(Long id, ResourceTemplateDTO resourceTemplateDTO);

    /**
     * Method deletes {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    void deleteById(Long id);

    /**
     * Method finds all {@link ResourceTemplate} by name or description.
     *
     * @param body map containing String key and String value
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    List<ResourceTemplateDTO> searchByNameOrDescriptionContaining(Map<String, String> body);
}