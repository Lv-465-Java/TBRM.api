package com.softserve.rms.repository;

import com.softserve.rms.entities.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserHistoryRepository {

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
     * Method that show all {@link User} history.
     *
     * @param id
     * @return list of all user's history
     * @author Mariia Shchur
     */
    List<Map<String, Object>> getUserHistory(Long id);

    /**
     * Method that returns all users history by accurate data
     *
     * @return list of all accounts
     * @author Mariia Shchur
     */
    List<Map<String, Object>> getAllByData(String date);

    /**
     * Method that return if token date+6hour is bigger then current date
     *
     * @param token a value of {@link Long}
     * @return map of data
     * @author Mariia Shchur
     */
    Map<String, Object> checkTokenDate(String token);
}
