package com.softserve.rms.service;

import com.softserve.rms.dto.resourceRecord.ResourceRecordDTO;

import java.util.List;

public interface FilterService {

    /**
     * Method checks if filter URL is or is not empty.
     *
     * @param filter    URL string with criteria
     * @param tableName name of a table where entities are searched
     * @return list of {@link ResourceRecordDTO}
     * @author Halyna Yatseniuk
     */
    List<ResourceRecordDTO> verifyIfFilterIsEmpty(String filter, String tableName);
}