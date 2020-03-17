package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.QueryConstants;
import com.softserve.rms.repository.UserHistoryRepository;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class UserHistoryRepositoryImpl implements UserHistoryRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserHistoryRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    @Override
    public List<Map<String, Object>> getDeletedAccounts() {
        return jdbcTemplate.queryForList(QueryConstants.DELETED_ACCOUNTS.getValue());
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    @Override
    public List<Map<String, Object>> getAllHistory() {
        return jdbcTemplate.queryForList(QueryConstants.ALL_HISTORY.getValue());
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    @Override
    public List<Map<String, Object>> getUserHistory(Long id) {

        return jdbcTemplate.queryForList(QueryConstants.USER_BY_ID.getValue(), id);
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
   @Override
    public List<Map<String, Object>> getAllByData(String date) {
        return jdbcTemplate.queryForList(QueryConstants.FILTER_BY_DATE.getValue(), date);
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    @Override
    public Map<String, Object> checkTokenDate(String token) {
        return jdbcTemplate.queryForMap(QueryConstants.IF_TOKEN_VALID.getValue(), token);
    }
}
