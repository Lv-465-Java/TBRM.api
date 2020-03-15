package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.QueryConstants;
import com.softserve.rms.repository.UserHistoryRepository;
import com.softserve.rms.service.UserHistoryService;
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
    private DSLContext dslContext;

    @Autowired
    public UserHistoryRepositoryImpl(DSLContext dslContext, DataSource dataSource) {
        this.dslContext = dslContext;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Map<String, Object>> getDeletedAccounts() {
        return jdbcTemplate.queryForList(QueryConstants.DELETED_ACCOUNTS.getValue());
    }

    @Override
    public List<Map<String, Object>> getAllAccounts() {
//        return dslContext.selectFrom(users_aud)
//                .join(roles).on(roles.id.eq(users_aud.role_id)).fetch();
        return jdbcTemplate.queryForList(QueryConstants.ALL_HISTORY.getValue());
    }

    @Override
    public List<Map<String, Object>> getUserHistory(Long id) {

        return jdbcTemplate.queryForList(QueryConstants.USER_BY_ID.getValue(), id);
    }

   @Override
    public List<Map<String, Object>> getAllByData(String date) {
        return jdbcTemplate.queryForList(QueryConstants.FILTER_BY_DATE.getValue(), date);
    }
}
