package com.softserve.rms.service.implementation;

import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.repository.implementation.JooqSearching;
import com.softserve.rms.service.SearchService;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.condition;
import static org.jooq.impl.DSL.field;

@Service
public class SearchServiceImpl implements SearchService {
    private JooqSearching jooqSearching;
    private DSLContext dslContext;
    private ModelMapper modelMapper;

    @Autowired
    public SearchServiceImpl(JooqSearching jooqSearching, DSLContext dslContext, ModelMapper modelMapper) {
        this.jooqSearching = jooqSearching;
        this.dslContext = dslContext;
        this.modelMapper = modelMapper;
    }

    public List<ResourceTemplateDTO> searchMethod() {
        Field<Object> film = field("creator_id");
        Field<Object> isPublished = field("is_published");

//        String myString = "is_published = false and creator_id = 5";
//        Condition withString = condition(myString);
//        Condition withField = field("name").containsIgnoreCase("Room");
//
//        List<Condition> list = new ArrayList<>();
//        list.add(withField);
//        list.add(withString);

        Map<Field<?>, Object> map = new HashMap<>();
        map.put(film, 5);
        map.put(isPublished, true);
        List<ResourceTemplate> resourceTemplates = jooqSearching.searchResourceTemplate(map);
        return resourceTemplates.stream()
                .map(resourceTemplate -> modelMapper.map(resourceTemplate, ResourceTemplateDTO.class))
                .collect(Collectors.toList());
    }

}