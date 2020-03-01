package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.service.UserService;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static org.jooq.impl.DSL.field;

@Repository
public class JooqSearch {

    private DSLContext dslContext;
    private UserService userService;

    public JooqSearch(DSLContext dslContext, UserService userService) {
        this.dslContext = dslContext;
        this.userService = userService;
    }

    public List<ResourceTemplate> searchResourceTemplate() {
        Result<Record> searchResult = dslContext.selectFrom("resource_templates")
                .where(field("name").eq("Room"))
//                .and(field("description").eq("Room"))
                .fetch();
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