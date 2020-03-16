package com.softserve.rms.service;

import com.softserve.rms.entities.User;

import java.util.List;
import java.util.Map;

public interface UserHistoryService {
    /**
     * Method that show all {@link User} history.
     *
     * @param id
     * @return list of all user's history
     * @author Mariia Shchur
     */
    List<Map<String, Object>> getUserHistory(Long id);

    /**
     * Method that returns all deleted accounts
     *
     * @return list of all deleted accounts
     * @author Mariia Shchur
     */
    List<Map<String, Object>> getDeletedAccounts();

    /**
     * Method that returns all users history flow
     *
     * @return list of all accounts
     * @author Mariia Shchur
     */
    List<Map<String, Object>> getAllHistory();

    /**
     * Method that returns all users history by accurate data
     *
     * @return list of all accounts
     * @author Mariia Shchur
     */
    List<Map<String, Object>> getAllByData(String date);
}
