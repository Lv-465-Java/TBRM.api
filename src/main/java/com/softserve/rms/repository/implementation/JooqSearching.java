package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.service.UserService;
import org.jooq.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.*;

//@Repository
public class JooqSearching {

    private DSLContext dslContext;
    private UserService userService;
    private Map<String, Object> myMap = new HashMap<>();

    public JooqSearching(DSLContext dslContext, UserService userService) {
        this.dslContext = dslContext;
        this.userService = userService;
    }
//    String string = new String("name=room");
//    Condition condition(string);
//    myMap.put("name", "room");


    String tab = new String("resource_templates");

    public List<ResourceTemplate> searchResourceTemplate(Map<Field<?>, Object> myMap) {
//        String myString = "is_published = false and creator_id = 5";
        Condition withString = condition("is_published = true and creator_id = 5");
        Condition withField = field("name").containsIgnoreCase("Room");

        List<Condition> list = new ArrayList<>();
        list.add(withField);
        list.add(withString);
        System.out.println(list);

//        String myStr = "name != 'Room'";
//        Condition my = condition(myMap);
        Result<Record> searchResult = dslContext.selectFrom(tab)
                .where(list).fetch();

//                .where(condition())
//                .where(field("is_published").contains(false));
//                .fetch();
        return convertOneRecordsToList(searchResult);
    }

    private List<ResourceTemplate> convertOneRecordsToList(List<Record> records) {
        List<ResourceTemplate> templates = new ArrayList<>();
        for (Record record : records) {
            ResourceTemplate template = convertRecordToResourceTemplate(record);
            templates.add(template);
        }
        return templates;
    }

    private ResourceTemplate convertRecordToResourceTemplate(Record record) {
        ResourceTemplate template = new ResourceTemplate();
        template.setId((Long) record.getValue(field(FieldConstants.ID.getValue()).getName()));
        template.setName((String) record.getValue(field(FieldConstants.NAME.getValue()).getName()));
        template.setTableName((String) record.getValue(field("table_name").getName()));
        template.setDescription((String) record.getValue(field(FieldConstants.DESCRIPTION.getValue()).getName()));
        template.setIsPublished((Boolean) record.getValue(field("is_published").getName()));
        template.setUser((userService.getById(
                (Long) record.getValue(field("creator_id").getName()))));
//        template.setResourceParameters();
        return template;
    }


}