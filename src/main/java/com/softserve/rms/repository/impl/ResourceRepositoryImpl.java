package com.softserve.rms.repository.impl;

import com.softserve.rms.controller.ResourceTemplateController;
import com.softserve.rms.entities.Resource;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.repository.ResourceRepository;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.service.UserService;
import org.jooq.*;

import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.field;

import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
public class ResourceRepositoryImpl implements ResourceRepository {
    private DSLContext dslContext;
    private ResourceTemplateRepository resourceTemplateRepository;
    private UserService userService;

    @Autowired
    public ResourceRepositoryImpl(DSLContext dslContext, ResourceTemplateRepository resourceTemplateRepository, UserService userService) {
        this.dslContext = dslContext;
        this.resourceTemplateRepository = resourceTemplateRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public void save(Resource resource) {
        InsertQuery<Record> insert = dslContext.insertQuery(DSL.table(resource.getResourceTemplate().getTableName()));
        insert.addValue(field("name"), resource.getName());
        insert.addValue(field("description"), resource.getDescription());
        insert.addValue(field("resource_template_id"), resource.getResourceTemplate().getId());
        insert.addValue(field("user_id"), resource.getUser().getId());
//        List<ResourceParameter> parameterList = resource.getResourceTemplate().getResourceParameters();
//        List<Object> parameterValues = resource.getParameters();
//        for(ResourceParameter parameter : parameterList) {
//            insert.addValue(DSL.field(parameter.getColumnName(), parameter.getParameterType().getSqlType()), );
//        }
        HashMap<String, Object> parameters = resource.getParameters();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            insert.addValue(field(entry.getKey()), entry.getValue());
        }
//        for (int i = 0; i < parameterList.size(); i++) {
//            insert.addValue(DSL.field(parameterList.get(i).getColumnName(),
//                    parameterList.get(i).getParameterType().getSqlType()), parameterValues.get(i));
//        }
        insert.execute();
//        Resource resource1 = insert.getReturnedRecord();
//        return insert.ge;
    }

    @Transactional
    @Override
    public void update(Resource resource) {

    }

    @Transactional(readOnly = true)
    @Override
    public List<Resource> findAll(String resourceName) {
        List<Record> records = dslContext.selectFrom(resourceName).fetch();
        return convertRecordsToResourceList(records);
    }

    @Transactional(readOnly = true)
    @Override
    public Resource findById(String resourceName, Long id) {
        String tableName = resourceTemplateRepository.findByName(resourceName).get().getTableName();
        Record record = dslContext.selectFrom(tableName).where(field("id").eq(id)).fetchOne();
        return convertRecordToResource(record);
    }

    @Transactional
    @Override
    public void delete(String resourceName, Long id) {
        dslContext.delete(table(resourceName))
                .where(field("id").eq(id))
                .execute();
    }

    private List<Resource> convertRecordsToResourceList(List<Record> records) {
        List<Resource> resources = new ArrayList<>();
        for (Record record : records) {
            Resource resource = convertRecordToResource(record);
            resources.add(resource);
        }
        return resources;
    }

    private Resource convertRecordToResource(Record record) {
        Resource resource = new Resource();
        resource.setId((Long) record.getValue(field("id").getName()));
        resource.setName((String) record.getValue(field("name").getName()));
        resource.setDescription((String) record.getValue(field("description").getName()));
        Long templateId = (Long) record.getValue(field("resource_template_id").getName());
        resource.setResourceTemplate(resourceTemplateRepository.findById(templateId).get());
        Long userId = (Long) record.getValue(field("user_id").getName());
        resource.setUser(userService.getById(userId));
        resource.setParameters(getParameters(record));
        return resource;
    }

    private HashMap<String, Object> getParameters(Record record) {
        HashMap<String, Object> parameters = new HashMap<>();
        for (int i = 5; i < record.size(); i++) {
            parameters.put(record.field(i).getName(), record.field(i).getName());
        }
        return parameters;
    }
}
