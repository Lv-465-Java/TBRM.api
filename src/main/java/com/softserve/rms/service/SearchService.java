package com.softserve.rms.service;

import com.softserve.rms.dto.template.ResourceTemplateDTO;
import org.springframework.data.domain.Page;

public interface SearchService {

    /**
     * Method checks if search URL is or is not empty.
     *
     * @param search URL string with criteria
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    Page<ResourceTemplateDTO> verifyIfSearchIsEmpty(String search, Integer page, Integer pageSize);
}