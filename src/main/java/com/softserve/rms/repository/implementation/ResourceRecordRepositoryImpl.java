package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.exceptions.NotDeletedException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.ResourceRecordRepository;
import com.softserve.rms.service.ResourceTemplateService;
import com.softserve.rms.service.UserService;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
        query.addValue(field(FieldConstants.NAME.getValue()), resourceRecord.getName());
        query.addValue(field(FieldConstants.DESCRIPTION.getValue()), resourceRecord.getDescription());
        query.addValue(field(FieldConstants.USER_ID.getValue()), resourceRecord.getUser().getId());
        query.addValue(field(FieldConstants.PHOTOS_NAMES.getValue()),resourceRecord.getPhotosNames());
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
        query.addValue(field(FieldConstants.PHOTOS_NAMES.getValue()),resourceRecord.getPhotosNames());
        Map<String, Object> parameters = resourceRecord.getParameters();
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            if (entry.getValue() != null) {
                query.addValue(field(entry.getKey()), entry.getValue());
            }
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
    public Page<ResourceRecord> findAll(String tableName, Integer page, Integer pageSize) {
        Long totalItems = Long.valueOf(dslContext.selectCount()
                        .from(tableName)
                        .fetchOne(0, int.class));
        List<Record> records = dslContext
                .selectFrom(tableName)
                .fetch();
        List<ResourceRecord> resourceRecords = convertRecordsToResourceList(records);
        return new PageImpl<>(resourceRecords, PageRequest.of(page, pageSize), totalItems);
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
        dslContext.delete(table(tableName))
                .where(field(FieldConstants.ID.getValue()).eq(id))
                .execute();
    }

    /**
     * Method converts records from DB to ResourceRecord class.
     *
     * @param records list of {@link Record}
     * @return list of {@link ResourceRecord}
     * @author Andrii Bren
     */
    private List<ResourceRecord> convertRecordsToResourceList(List<Record> records) {
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
    private ResourceRecord convertRecordToResource(Record record) {
        Long userId = (Long) record.getValue(field(FieldConstants.USER_ID.getValue()).getName());
        return ResourceRecord.builder()
                .id((Long) record.getValue(field(FieldConstants.ID.getValue()).getName()))
                .name((String) record.getValue(field(FieldConstants.NAME.getValue()).getName()))
                .description((String) record.getValue(field(FieldConstants.DESCRIPTION.getValue()).getName()))
                .user(userService.getById(userId))
                .photosNames((String) record.getValue(field(FieldConstants.PHOTOS_NAMES.getValue()).getName()))
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
    private Map<String, Object> getParameters(Record record) {
        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < record.size(); i++) {
            if (record.field(i).getName().endsWith("_coordinate")) {
                parameters.put("coordinates", getAllCoordinates((String) record.getValue(i)));
            } else {
                parameters.put(record.field(i).getName(), record.getValue(i));
            }
        }
        parameters.remove(FieldConstants.ID.getValue());
        parameters.remove(FieldConstants.NAME.getValue());
        parameters.remove(FieldConstants.DESCRIPTION.getValue());
        parameters.remove(FieldConstants.USER_ID.getValue());
        parameters.remove(FieldConstants.PHOTOS_NAMES.getValue());

        return parameters;
    }

    private List<String> getLatitudeAndLongitude(String name) {
        return Arrays.asList(name.split(","));
    }

    private List<String> getCoordinate(String name) {
        return Arrays.asList(name.split(";"));
    }

    private List<Map<String, Double>> getAllCoordinates(String coordinateRecord) {
        List<Map<String, Double>> coordinates = new ArrayList<>();
        getCoordinate(coordinateRecord).forEach(element -> {
            Map<String, Double> coordinate = new LinkedHashMap<>();
            coordinate.put(FieldConstants.LATITUDE.getValue(),
                    Double.parseDouble(getLatitudeAndLongitude(element).get(0)));
            coordinate.put(FieldConstants.LONGITUDE.getValue(),
                    Double.parseDouble(getLatitudeAndLongitude(element).get(1)));
            coordinates.add(coordinate);
        });
        return coordinates;
    }
}
