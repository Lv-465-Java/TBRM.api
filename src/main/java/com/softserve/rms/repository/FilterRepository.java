package com.softserve.rms.repository;

import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.SearchCriteria;
import org.jooq.Condition;

import java.util.List;

public interface FilterRepository {
    /**
     * Method converts {@link SearchCriteria} to {@link Condition} due to search criteria operation type.
     *
     * @param conditionList list of {@link Condition}
     * @param tableName     name of a table where entities are searched
     * @return list of filtered {@link ResourceRecord}
     * @author Halyna Yatseniuk
     */
    List<ResourceRecord> searchResourceTemplate(List<Condition> conditionList, String tableName);
}