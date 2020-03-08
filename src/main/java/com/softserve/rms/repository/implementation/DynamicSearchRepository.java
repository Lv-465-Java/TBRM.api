package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.SearchCriteria;
import com.softserve.rms.exceptions.SqlGrammarException;
import org.jooq.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DynamicSearchRepository {
    private DSLContext dslContext;
    private ResourceRecordRepositoryImpl resourceRepository;

    /**
     * Constructor with parameters
     *
     * @author Halyna Yatseniuk
     */
    @Autowired
    public DynamicSearchRepository(DSLContext dslContext, ResourceRecordRepositoryImpl resourceRepository) {
        this.dslContext = dslContext;
        this.resourceRepository = resourceRepository;
    }

    /**
     * Method converts {@link SearchCriteria} to {@link Condition} due to search criteria operation type.
     *
     * @param conditionList list of {@link Condition}
     * @param tableName     name of a table where entities are searched
     * @return list of filtered {@link ResourceRecord}
     * @author Halyna Yatseniuk
     */
    public List<ResourceRecord> searchResourceTemplate(List<Condition> conditionList, String tableName) {
        Result<Record> searchResult;
        try {
            searchResult = dslContext.selectFrom(tableName)
                    .where(conditionList).fetch();
        } catch (BadSqlGrammarException exception) {
            throw new SqlGrammarException(ErrorMessage.WRONG_SEARCH_CRITERIA.getMessage());
        }
        return resourceRepository.convertRecordsToResourceList((searchResult));
    }
}