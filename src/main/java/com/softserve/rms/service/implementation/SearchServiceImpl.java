package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.repository.implementation.JooqSearch;
import com.softserve.rms.service.SearchService;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    private JooqSearch jooqSearch;
    private DSLContext dslContext;
    private ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public SearchServiceImpl(JooqSearch jooqSearch, DSLContext dslContext, ModelMapper modelMapper) {
        this.jooqSearch = jooqSearch;
        this.dslContext = dslContext;
        this.modelMapper = modelMapper;
    }

    public List<ResourceTemplateDTO> searchMethod() {
        List<ResourceTemplate> resourceTemplates = jooqSearch.searchResourceTemplate();
        return resourceTemplates.stream()
                .map(resourceTemplate -> modelMapper.map(resourceTemplate, ResourceTemplateDTO.class))
                .collect(Collectors.toList());
    }

}