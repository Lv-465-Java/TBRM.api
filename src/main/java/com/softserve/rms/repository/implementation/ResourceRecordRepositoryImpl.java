package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.exceptions.NotDeletedException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.ResourceRecordRepository;
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
    private UserService userService;

    /**
     * Constructor with parameters
     *
     * @author Andrii Bren
     */
    @Autowired
    public ResourceRecordRepositoryImpl(DSLContext dslContext, UserService userService) {
        this.dslContext = dslContext;
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
        query.addValue(field(FieldConstants.NAME.getValue()), resourceRecord.getName());
        query.addValue(field(FieldConstants.DESCRIPTION.getValue()), resourceRecord.getDescription());
        query.addValue(field(FieldConstants.USER_ID.getValue()), resourceRecord.getUser().getId());
        Map<String, Object> parameters = resourceRecord.getParameters();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.addValue(field(entry.getKey()), entry.getValue());
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
        query.addValue(field(FieldConstants.NAME.getValue()), resourceRecord.getName());
        query.addValue(field(FieldConstants.DESCRIPTION.getValue()), resourceRecord.getDescription());
        Map<String, Object> parameters = resourceRecord.getParameters();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.addValue(field(entry.getKey()), entry.getValue());
        }
        query.addConditions(field(FieldConstants.ID.getValue()).eq(id));
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
        Result<Record> records = dslContext.selectFrom(tableName).fetch();
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
        Record record = dslContext.selectFrom(tableName).where(field(FieldConstants.ID.getValue()).eq(id)).fetchOne();
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
    public void delete(String tableName, Long id) throws NotFoundException, NotDeletedException {
        int deleted = dslContext.delete(table(tableName))
                .where(field(FieldConstants.ID.getValue()).eq(id))
                .execute();
        if (deleted == 0) {
            throw new NotDeletedException(ErrorMessage.RESOURCE_CAN_NOT_BE_DELETED_BY_ID.getMessage() + id);
        }
    }

    /**
     * Method converts records from DB to ResourceRecord class.
     *
     * @param records list of {@link Record}
     * @return list of {@link ResourceRecord}
     * @author Andrii Bren
     */
    public List<ResourceRecord> convertRecordsToResourceList(Result<Record> records) {
        List<ResourceRecord> resourceRecords = new ArrayList<>();
        for (Record record : records) {
            ResourceRecord resourceRecord = convertRecordToResource(record);
            resourceRecords.add(resourceRecord);
        }
        return resourceRecords;
    }

    /**
     * Method converts record from DB to ResourceRecord class.
     *
     * @param record instance of {@link Record}
     * @return instance of {@link ResourceRecord}
     * @author Andrii Bren
     */
    public ResourceRecord convertRecordToResource(Record record) {
        Long userId = (Long) record.getValue(field(FieldConstants.USER_ID.getValue()).getName());
        return ResourceRecord.builder()
                .id((Long) record.getValue(field(FieldConstants.ID.getValue()).getName()))
                .name((String) record.getValue(field(FieldConstants.NAME.getValue()).getName()))
                .description((String) record.getValue(field(FieldConstants.DESCRIPTION.getValue()).getName()))
                .user(userService.getById(userId))
                .parameters(getParameters(record))
                .build();
    }

    /**
     * Method gets all dynamic resource parameters records from DB.
     *
     * @param record instance of {@link Record}
     * @return map of dynamic resource parameters
     * @author Andrii Bren
     */
    public Map<String, Object> getParameters(Record record) {
        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < record.size(); i++) {
            parameters.put(record.field(i).getName(), record.getValue(i));
        }
        parameters.remove(FieldConstants.ID.getValue());
        parameters.remove(FieldConstants.NAME.getValue());
        parameters.remove(FieldConstants.DESCRIPTION.getValue());
        parameters.remove(FieldConstants.USER_ID.getValue());
        return parameters;
    }
}
