package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.exceptions.NotDeletedException;
import com.softserve.rms.exceptions.NotFoundException;
import com.softserve.rms.repository.ResourceRecordRepository;
import com.softserve.rms.repository.ResourceTemplateRepository;
import com.softserve.rms.service.UserService;
import org.jooq.DSLContext;
import org.jooq.InsertQuery;
import org.jooq.Record;
import org.jooq.UpdateQuery;
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
    private UserService userService;
    private ResourceTemplateRepository resourceTemplateRepository;

    /**
     * Constructor with parameters
     *
     * @author Andrii Bren
     */
    @Autowired
    public ResourceRecordRepositoryImpl(DSLContext dslContext, UserService userService,
                                        ResourceTemplateRepository resourceTemplateRepository) {
        this.dslContext = dslContext;
        this.userService = userService;
        this.resourceTemplateRepository = resourceTemplateRepository;
    }

    /**
     * {@inheritDoc}
     *
     * @author Andrii Bren
     */
    @Transactional
    @Override
    public void save(String tableName, ResourceRecord resourceRecord) {
        resourceTemplateRepository.findByTableName(tableName);
        InsertQuery<Record> query = dslContext.insertQuery(table(tableName));
        query.addValue(field(FieldConstants.NAME.getValue()), resourceRecord.getName());
        query.addValue(field(FieldConstants.DESCRIPTION.getValue()), resourceRecord.getDescription());
        query.addValue(field(FieldConstants.USER_ID.getValue()), resourceRecord.getUser().getId());
        query.addValue(field(FieldConstants.PHOTOS_NAMES.getValue()), resourceRecord.getPhotosNames());
        query.addValue(field(FieldConstants.DOCUMENTS_NAMES.getValue()), resourceRecord.getDocumentNames());
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
        resourceTemplateRepository.findByTableName(tableName);
        UpdateQuery<Record> query = dslContext.updateQuery(table(tableName));
        query.addValue(field(FieldConstants.NAME.getValue()), resourceRecord.getName());
        query.addValue(field(FieldConstants.DESCRIPTION.getValue()), resourceRecord.getDescription());
        query.addValue(field(FieldConstants.PHOTOS_NAMES.getValue()), resourceRecord.getPhotosNames());
        query.addValue(field(FieldConstants.DOCUMENTS_NAMES.getValue()), resourceRecord.getDocumentNames());
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
        resourceTemplateRepository.findByTableName(tableName);
        Long totalItems = Long.valueOf(dslContext.selectCount()
                .from(tableName)
                .fetchOne(0, int.class));
        List<Record> records = dslContext
                .selectFrom(tableName)
                .limit(inline(pageSize))
                .offset(inline(page * pageSize))
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
        resourceTemplateRepository.findByTableName(tableName);
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
        resourceTemplateRepository.findByTableName(tableName);
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
    public List<ResourceRecord> convertRecordsToResourceList(List<Record> records) {
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
                .documentNames(((String) record.getValue(field(FieldConstants.DOCUMENTS_NAMES.getValue()).getName())))
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
            if (record.field(i).getName().endsWith(FieldConstants.COORDINATE.getValue())) {
                parameters.put(record.field(i).getName(), getAllCoordinates((String) record.getValue(i)));
            } else {
                parameters.put(record.field(i).getName(), record.getValue(i));
            }
        }
        parameters.remove(FieldConstants.ID.getValue());
        parameters.remove(FieldConstants.NAME.getValue());
        parameters.remove(FieldConstants.DESCRIPTION.getValue());
        parameters.remove(FieldConstants.USER_ID.getValue());
        parameters.remove(FieldConstants.PHOTOS_NAMES.getValue());
        parameters.remove(FieldConstants.DOCUMENTS_NAMES.getValue());

        return parameters;
    }

    /**
     * Method gets formatted latitude and longitude from DB.
     *
     * @param coordinate specified coordinate of point
     * @return list of formatted latitudes and longitudes
     * @author Andrii Bren
     */
    private List<String> getLatitudeAndLongitude(String coordinate) {
        return Arrays.asList(coordinate.split(","));
    }

    /**
     * Method gets formatted coordinate from DB.
     *
     * @param coordinateRecord specified coordinate record
     * @return list of formatted coordinates
     * @author Andrii Bren
     */
    private List<String> getCoordinate(String coordinateRecord) {
        return Arrays.asList(coordinateRecord.split(";"));
    }

    /**
     * Method gets all coordinates from DB.
     *
     * @param coordinateRecord specified coordinate record
     * @return list of coordinates
     * @author Andrii Bren
     */
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