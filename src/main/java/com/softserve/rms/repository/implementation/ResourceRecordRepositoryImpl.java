package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.ResourceRecordRepository;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.service.UserService;
import org.jooq.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.jooq.impl.DSL.*;

/**
 * Implementation of {@link ResourceRecordRepository}
 *
 * @author Andrii Bren
 */
@Repository
public class ResourceRecordRepositoryImpl implements ResourceRecordRepository {
    private DSLContext dslContext;
    private ResourceTemplateService resourceTemplateService;
    private UserService userService;

    /**
     * Constructor with parameters
     *
     * @author Andrii Bren
     */
    @Autowired
    public ResourceRecordRepositoryImpl(DSLContext dslContext, ResourceTemplateService resourceTemplateService, UserService userService) {
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
    public void save(String tableName, ResourceRecord resourceRecord) {
        InsertQuery<Record> query = dslContext.insertQuery(table(tableName));
        query.addValue(field("name"), resourceRecord.getName());
        query.addValue(field("description"), resourceRecord.getDescription());
        query.addValue(field("resource_template_id"), resourceRecord.getResourceTemplate().getId());
        query.addValue(field("user_id"), resourceRecord.getUser().getId());
        if (resourceRecord.getParameters() != null) {
            Map<String, Object> parameters = resourceRecord.getParameters();
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                query.addValue(field(entry.getKey()), entry.getValue());
            }
        }
        query.execute();
    }


    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Transactional
    @Override
    public void update(String tableName, Long id, ResourceRecord resourceRecord) {
        UpdateQuery<Record> query = dslContext.updateQuery(table(tableName));
        query.addValue(field("name"), resourceRecord.getName());
        query.addValue(field("description"), resourceRecord.getDescription());
        Map<String, Object> parameters = resourceRecord.getParameters();
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
    public List<ResourceRecord> findAll(String tableName) {
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
    public Optional<ResourceRecord> findById(String tableName, Long id)
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

    private List<ResourceRecord> convertRecordsToResourceList(List<Record> records) {
        List<ResourceRecord> resourceRecords = new ArrayList<>();
        for (Record record : records) {
            ResourceRecord resourceRecord = convertRecordToResource(record);
            resourceRecords.add(resourceRecord);
        }
        return resourceRecords;
    }


    private ResourceRecord convertRecordToResource(Record record) {
        Long templateId = (Long) record.getValue(field("resource_template_id").getName());
        Long userId = (Long) record.getValue(field("user_id").getName());
        return ResourceRecord.builder()
                .id((Long) record.getValue(field("id").getName()))
                .name((String) record.getValue(field("name").getName()))
                .description((String) record.getValue(field("description").getName()))
                .resourceTemplate(resourceTemplateService.findEntityById(templateId))
                .user(userService.getById(userId))
                .parameters(getParameters(record))
                .build();
    }

    private Map<String, Object> getParameters(Record record) {
        Map<String, Object> parameters = new HashMap<>();
        for (int i = 5; i < record.size(); i++) {
            parameters.put(record.field(i).getName(), record.getValue(i));
        }
        return parameters;
    }
}
