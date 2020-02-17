package com.softserve.rms.repository.impl;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.entities.Resource;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.ResourceRepository;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.service.UserService;
import org.jooq.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.jooq.impl.DSL.*;

/**
 * Implementation of {@link ResourceRepository}
 *
 * @author Andrii Bren
 */
@Repository
public class ResourceRepositoryImpl implements ResourceRepository {
    private DSLContext dslContext;
    private ResourceTemplateService resourceTemplateService;
    private UserService userService;

    /**
     * Constructor with parameters
     *
     * @author Andrii Bren
     */
    @Autowired
    public ResourceRepositoryImpl(DSLContext dslContext, ResourceTemplateService resourceTemplateService, UserService userService) {
        this.dslContext = dslContext;
        this.resourceTemplateService = resourceTemplateService;
        this.userService = userService;
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Transactional
    @Override
    public void save(String tableName, Resource resource) {
        InsertQuery<Record> query = dslContext.insertQuery(table(tableName));
        query.addValue(field("name"), resource.getName());
        query.addValue(field("description"), resource.getDescription());
        query.addValue(field("resource_template_id"), resource.getResourceTemplate().getId());
        query.addValue(field("user_id"), resource.getUser().getId());
        if (resource.getParameters() != null) {
            HashMap<String, Object> parameters = resource.getParameters();
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.addValue(field(entry.getKey()), entry.getValue());
            }
        }
        query.execute();
//        InsertSetStep<Record> insert = dslContext.insertInto(table(tableName));
//        insert.set(field("name"), resource.getName());
//        insert.set(field("description"), resource.getDescription());
//        insert.set(field("resource_template_id"), resource.getResourceTemplate().getId());
//        insert.set(field("user_id"), resource.getUser().getId());
//        HashMap<String, Object> parameters = resource.getParameters();
//        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
//            insert.set(field(entry.getKey()), entry.getValue());
//        }

//        InsertSetStep<Record> insert = dslContext.insertInto(table(tableName)).;

//        Record record = dslContext.insertInto(table(tableName))
//                .set(field("name"), resource.getName())
//                .set(field("description"), resource.getDescription())
//                .set(field("resource_template_id"), resource.getResourceTemplate().getId())
//                .set(field("user_id"), resource.getUser().getId())
//                .returning().fetchOne();

//        Record record = insert.;
//        return convertRecordToResource(record);
//        return null;
    }


    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Transactional
    @Override
    public void update(String tableName, Long id, Resource resource) {
        UpdateQuery<Record> query = dslContext.updateQuery(table(tableName));
        query.addValue(field("name"), resource.getName());
        query.addValue(field("description"), resource.getDescription());
        HashMap<String, Object> parameters = resource.getParameters();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.addValue(field(entry.getKey()), entry.getValue());
        }
        query.addConditions(field("id").eq(id));
        query.execute();
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Transactional(readOnly = true)
    @Override
    public List<Resource> findAll(String tableName) {
        List<Record> records = dslContext.selectFrom(tableName).fetch();
        return convertRecordsToResourceList(records);
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Transactional(readOnly = true)
    @Override
    public Optional<Resource> findById(String tableName, Long id)
            throws NotFoundException {
        Record record = dslContext.selectFrom(tableName).where(field("id").eq(id)).fetchOne();
        if (record == null) {
            throw new NotFoundException(ErrorMessage.CAN_NOT_FIND_A_RESOURCE_BY_ID.getMessage() + id);
        }
        return Optional.of(convertRecordToResource(record));
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Transactional
    @Override
    public void delete(String tableName, Long id) throws NotFoundException {
        findById(tableName, id);
        dslContext.delete(table(tableName))
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
        resource.setResourceTemplate(resourceTemplateService.findEntityById(templateId));
        Long userId = (Long) record.getValue(field("user_id").getName());
        resource.setUser(userService.getById(userId));
        resource.setParameters(getParameters(record));
        return resource;
    }

    private HashMap<String, Object> getParameters(Record record) {
        HashMap<String, Object> parameters = new HashMap<>();
        for (int i = 5; i < record.size(); i++) {
            parameters.put(record.field(i).getName(), record.getValue(i));
        }
        return parameters;
    }
}
