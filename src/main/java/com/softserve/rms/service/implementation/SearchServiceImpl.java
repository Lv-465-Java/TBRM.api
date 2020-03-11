package com.softserve.rms.service.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.InvalidParametersException;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.search.SearchAndFilterUtil;
import com.softserve.rms.search.SpecificationsBuilder;
import com.softserve.rms.service.SearchService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private ModelMapper modelMapper;
    private ResourceTemplateRepository templateRepository;
    private ResourceTemplateServiceImpl templateService;
    private SearchAndFilterUtil searchAndFilterUtil;

    /**
     * Constructor with parameters
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public SearchServiceImpl(ModelMapper modelMapper, ResourceTemplateRepository templateRepository,
                             ResourceTemplateServiceImpl templateService, SearchAndFilterUtil searchAndFilterUtil) {
        this.modelMapper = modelMapper;
        this.templateRepository = templateRepository;
        this.templateService = templateService;
        this.searchAndFilterUtil = searchAndFilterUtil;
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    @Override
    public List<ResourceTemplateDTO> verifyIfSearchIsEmpty(String search) {
        if (search.isEmpty()) {
            return templateService.getAll();
        } else {
            return searchBySpecification(search, FieldConstants.RESOURCE_TEMPLATES_TABLE.getValue());
        }
    }

    /**
     * Method searches {@link ResourceTemplate} by provided search parameters.
     *
     * @param search    URL string with parameters
     * @param tableName name of a table where entities are searched
     * @return list of {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */
    private List<ResourceTemplateDTO> searchBySpecification(String search, String tableName) {
        SpecificationsBuilder builder = new SpecificationsBuilder(searchAndFilterUtil);
        Specification<ResourceTemplate> specification = builder.buildSpecification(search, tableName);

        List<ResourceTemplate> list;
        try {
            list = templateRepository.findAll(specification);
        } catch (IllegalArgumentException ex) {
            throw new InvalidParametersException(ErrorMessage.INVALID_SEARCH_CRITERIA.getMessage());
        }
        return list.stream()
                .map(template -> modelMapper.map(template, ResourceTemplateDTO.class))
                .collect(Collectors.toList());
    }
}