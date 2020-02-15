package com.softserve.rms.repository.impl;

import com.softserve.rms.controller.ResourceTemplateController;
import com.softserve.rms.entities.Resource;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.repository.ResourceRepository;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.service.UserService;
import org.jooq.*;

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
        insert.addValue(DSL.field("name"), resource.getName());
        insert.addValue(DSL.field("description"), resource.getDescription());
        insert.addValue(DSL.field("resource_template_id"), resource.getResourceTemplate().getId());
        insert.addValue(DSL.field("user_id"), resource.getUser().getId());
//        List<ResourceParameter> parameterList = resource.getResourceTemplate().getResourceParameters();
//        List<Object> parameterValues = resource.getParameters();
//        for(ResourceParameter parameter : parameterList) {
//            insert.addValue(DSL.field(parameter.getColumnName(), parameter.getParameterType().getSqlType()), );
//        }
        HashMap<String, Object> parameters = resource.getParameters();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            insert.addValue(DSL.field(entry.getKey()), entry.getValue());
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

    @Override
    public List<Resource> findAll(String resourceName) {
//        ResourceTemplate resourceTemplate = resourceTemplateRepository.findByName(resourceName).get();
//        List<Resource> resources = new ArrayList<>();
//        String sql = dslContext.select()
//        List<Record> query = dslContext.selectFrom("room").fetchInto(Record.class);
//        List<Resource> resourceList = convertQueryResultToModelObject(query);


//        Result<Record> result = dslContext.select().from(resourceTemplate.getTableName()).getResult();
////        String sql = dslContext.select(field())
//        StringBuilder sb = new StringBuilder();
//        resourceTemplate.getResourceParameters().forEach(resourceParameter -> sb.append("field(")
//                .append(resourceParameter.getColumnName()).append("),"));
//        SelectQuery<Record> query = dslContext.selectQuery();
//        return query.fetch()
        List<Record> records = dslContext.selectFrom(resourceName).fetch();
        return convertRecordsToResourceList(records);

//        Result<Record> result = dslContext.select().from(resourceTemplate.getTableName()).fetch();
//        return result.map(record -> );
//        return null;
//
//        List<Resource> resources =
//        dslContext.selectOne().fetch();
//        List<SelectQuery<?>> selectQuery = dslContext.select().from(resourceTemplate.getTableName()).getSelect();

//        SelectQuery<?> selectQuery = dslContext.selectQuery(DSL.table(resourceTemplate.getTableName()));
//        selectQuery.addConditions();
//        selectQuery.addSelect();
//        StringBuilder sql = dslContext.select()
//        return dslContext.meta().getTables()
//                .stream().filter(resource -> !ignoredTables().contains(resource.getName()));
    }
//
//    private List<Todo> convertQueryResultsToModelObjects(List<TodosRecord> queryResults) {
//        List<Todo> todoEntries = new ArrayList<>();
//
//        for (TodosRecord queryResult : queryResults) {
//            Todo todoEntry = convertQueryResultToModelObject(queryResult);
//            todoEntries.add(todoEntry);
//        }
//
//        return todoEntries;
//    }
//
//    private Todo convertQueryResultToModelObject(TodosRecord queryResult) {
//        return Todo.getBuilder(queryResult.getTitle())
//                .creationTime(queryResult.getCreationTime())
//                .description(queryResult.getDescription())
//                .id(queryResult.getId())
//                .modificationTime(queryResult.getModificationTime())
//                .build();
//    }

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
        resource.setId((Long) record.getValue(DSL.field("id").getName()));
        resource.setName((String) record.getValue(DSL.field("name").getName()));
        resource.setDescription((String) record.getValue(DSL.field("description").getName()));
        Long templateId = (Long) record.getValue(DSL.field("resource_template_id").getName());
        resource.setResourceTemplate(resourceTemplateRepository.findById(templateId).get());
        Long userId = (Long) record.getValue(DSL.field("user_id").getName());
        resource.setUser(userService.getById(userId));
//        resource.setDescription(records.field(DSL.field("description")));
//        resource.setDescription((String) records.get(1).getValue("description"));

        Integer size = record.size();


//        parameters.put("number", 2);
//        parameters.put("testint", 32);
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

    @Transactional(readOnly = true)
    @Override
    public Resource findById(String resourceName, Long id) {
        String tableName = resourceTemplateRepository.findByName(resourceName).get().getTableName();
        Record record = dslContext.selectFrom(tableName).where(DSL.field("id").eq(id)).fetchOne();
        return convertRecordToResource(record);
    }

    @Override
    public void delete(Long id) {
//        dslContext.delete()
    }
}
