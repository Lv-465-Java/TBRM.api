package com.softserve.rms.service;

import com.softserve.rms.dto.template.ResourceTemplateDTO;

import java.util.List;

public interface SearchService {

    /**
     * Method checks if search URL is or is not empty.
     *
     * @param search URL string with criteria
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    List<ResourceTemplateDTO> verifyIfSearchIsEmpty(String search);
}