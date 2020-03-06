package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.exceptions.SqlGrammarException;
import com.softserve.rms.service.UserService;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;

import java.util.List;

//@Repository
public class JooqSearch {
    private DSLContext dslContext;
    private ResourceRecordRepositoryImpl resourceRepository;

    @Autowired
    public JooqSearch(DSLContext dslContext, ResourceRecordRepositoryImpl resourceRepository) {
        this.dslContext = dslContext;
        this.resourceRepository = resourceRepository;
    }

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