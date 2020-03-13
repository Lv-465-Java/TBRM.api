package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.exceptions.SqlGrammarException;
import com.softserve.rms.repository.FilterRepository;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FilterRepositoryImpl implements FilterRepository {
    private DSLContext dslContext;
    private ResourceRecordRepositoryImpl resourceRepository;

    /**
     * Constructor with parameters
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public FilterRepositoryImpl(DSLContext dslContext, ResourceRecordRepositoryImpl resourceRepository) {
        this.dslContext = dslContext;
        this.resourceRepository = resourceRepository;
    }

    /**
     * {@inheritDoc}
     *
     * @author Halyna Yatseniuk
     */
    public List<ResourceRecord> searchResourceTemplate(List<Condition> conditionList, String tableName) {
        List<Record> searchResult;
        try {
            searchResult = dslContext.selectFrom(tableName)
                    .where(conditionList).fetch();
        } catch (BadSqlGrammarException exception) {
            throw new SqlGrammarException(ErrorMessage.WRONG_SEARCH_CRITERIA.getMessage());
        }
        return resourceRepository.convertRecordsToResourceList((searchResult));
    }
}