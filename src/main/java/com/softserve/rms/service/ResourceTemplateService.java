package com.softserve.rms.service;

import com.softserve.rms.dto.template.ResourceTemplateSaveDTO;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.entities.Person;
import com.softserve.rms.exceptions.NoSuchEntityException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateIsPublishedException;
import com.softserve.rms.exceptions.resourseTemplate.ResourceTemplateParameterListIsEmpty;

import java.util.List;
import java.util.Map;

public interface ResourceTemplateService {
    /**
     * Method creates {@link ResourceTemplateSaveDTO}.
     *
     * @param resourceTemplateSaveDTO {@link ResourceTemplateSaveDTO}
     * @return new {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    ResourceTemplateDTO save(ResourceTemplateSaveDTO resourceTemplateSaveDTO);

    /**
     * Method finds {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    ResourceTemplateDTO getById(Long id);

    /**
     * Method finds all {@link ResourceTemplate} by person id.
     *
     * @param id of {@link Person}
     * @return List of all {@link ResourceTemplateDTO} for person
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
    ResourceTemplateDTO updateById(Long id, ResourceTemplateSaveDTO resourceTemplateSaveDTO);

    /**
     * Method deletes {@link ResourceTemplate} by id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    boolean deleteById(Long id);

    /**
     * Method finds all {@link ResourceTemplate} by name or description.
     *
     * @param body map containing String key and String value
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    List<ResourceTemplateDTO> searchByNameOrDescriptionContaining(Map<String, String> body);

    /**
     * Method finds  {@link ResourceTemplate} by provided id.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplate}
     * @throws NoSuchEntityException if the resource template with provided id is not found
     * @author Halyna Yatseniuk
     */

    ResourceTemplate findById(Long id);

    /**
     * Method makes {@link ResourceTemplate} be published.
     *
     * @param id of {@link ResourceTemplateDTO}
     * @return boolean value of {@link ResourceTemplateDTO} isPublished field
     * @throws ResourceTemplateIsPublishedException if resource template has been published already
     * @throws ResourceTemplateParameterListIsEmpty if resource template do not have attached parameters
     * @author Halyna Yatseniuk
     */
    Boolean publishResourceTemplate(Long id);
}