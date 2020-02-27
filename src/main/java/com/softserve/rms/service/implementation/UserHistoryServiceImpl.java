package com.softserve.rms.service.implementation;

import com.softserve.rms.service.UserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class UserHistoryServiceImpl implements UserHistoryService {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public UserHistoryServiceImpl(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private Object password = new Object();
    private Object first_name = new Object();
    private Object last_name = new Object();
    private Object phone = new Object();
    private Object email = new Object();
    private Object reset_token = new Object();
    private Boolean passwordChange = false;

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    final static String USER_BY_ID = "select u.revtype,to_timestamp(r.revtstmp/ 1000)::timestamp,u.first_name, u.last_name, u.email, u.phone,u.password,\n" +
            "u.reset_token from users_aud u ,revinfo r where  \n" +
            "u.rev=r.rev and u.id = ?";

    public List<Map<String, Object>> getUserHistory(Long id) {
        List<Map<String, Object>> data = jdbcTemplate.queryForList(USER_BY_ID, id);
        for (int i = 0; i < data.size(); i++) {
            Iterator<Map.Entry<String, Object>> iterator = data.get(i).entrySet().iterator();
            if (i == 0) {
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> map = iterator.next();
                    rememberDataWhenCreate(map);
                    if(map.getKey().equals("password")|| map.getKey().equals("reset_token")){
                        iterator.remove();
                    }
                }
                continue;
            }
            while (iterator.hasNext()) {
                Map.Entry<String, Object> map = iterator.next();
                if (map.getKey().equals("revtype") && (map.getValue().equals(1))) {
                    iterator.remove();
                }
                passwordMessage(iterator, map);
                this.first_name = setMessage(iterator, map, "first_name", this.first_name);
                this.last_name = setMessage(iterator, map, "last_name", this.last_name);
                this.phone = setMessage(iterator, map, "phone", this.phone);
                this.email = setMessage(iterator, map, "email", this.email);
                resetTokenMessage(iterator, map);
            }
            passwordChange = false;
        }
        return data;
    }

    /**
     * Method that compare user's password from history
     * flow and set user's friendly message
     *
     * @param iterator
     * @param map      list of history flow
     * @author Mariia Shchur
     */
    private void passwordMessage(Iterator<Map.Entry<String, Object>> iterator,
                                 Map.Entry<String, Object> map) {
        if (map.getKey().equals("password")) {
            if (!(map.getValue().equals(password))) {
                password = (String) map.getValue();
                map.setValue("Password has been changed");
                passwordChange = true;
            } else {
                iterator.remove();
            }
        }
    }

    /**
     * Method that compare user's reset token from history
     * flow and set user's friendly message
     *
     * @param iterator
     * @param map      list of history flow
     * @author Mariia Shchur
     */
    private void resetTokenMessage(Iterator<Map.Entry<String, Object>> iterator,
                                   Map.Entry<String, Object> map) {
        if (map.getKey().equals("reset_token")) {
            if (map.getValue() != reset_token) {
                reset_token = (String) map.getValue();
                if (passwordChange) {
                    map.setValue("Password was reseted by link from email");
                } else map.setValue("Link for password resenting was sent on email");
            } else {
                iterator.remove();
            }
        }
    }

    /**
     * Method that compare user's data from history
     * flow and set user's friendly message when it's changed and
     * delete row if data is unchanged
     *
     * @param iterator
     * @param map      list of history flow
     * @param keyName
     * @param value
     * @author Mariia Shchur
     */
    private Object setMessage(Iterator<Map.Entry<String, Object>> iterator,
                              Map.Entry<String, Object> map, String keyName, Object value) {
        if (map.getKey().equals(String.format(keyName))) {
            if (!(map.getValue().equals(value))) {
                value = map.getValue();
                map.setValue(String.format("%s has been changed to " + value, keyName));
            } else {
                iterator.remove();
            }
        }
        return value;
    }

    /**
     * Method that remembers user's registration data
     *
     * @param map list of history flow
     * @author Mariia Shchur
     */
    private void rememberDataWhenCreate(Map.Entry<String, Object> map) {
        if (map.getKey().equals("revtype") && (map.getValue().equals(0))) {
            map.setValue("Create account");
        }
        if (map.getKey().equals("password")) {
            this.password = (String) map.getValue();
        }
        if (map.getKey().equals("email")) {
            this.email = (String) map.getValue();
        }
        if (map.getKey().equals("phone")) {
            this.phone = (String) map.getValue();
        }
        if (map.getKey().equals("first_name")) {
            this.first_name = (String) map.getValue();
        }
        if (map.getKey().equals("last_name")) {
            this.last_name = (String) map.getValue();
        }
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    final static String DELETED_ACCOUNTS = "select to_timestamp(r.revtstmp/ 1000)::timestamp ,u.first_name,u.last_name, u.email, u.phone from users_aud u,\n" +
            "revinfo r where u.revtype=2 and u.rev=r.rev";

    public List<Map<String, Object>> getDeletedAccounts() {
        return jdbcTemplate.queryForList(DELETED_ACCOUNTS);
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    final static String ALL_ACCOUNTS = "select u.revtype,u.first_name,u.last_name,u.email,u.enabled,u.phone, to_timestamp(r.revtstmp/ 1000)  from users_aud u ,revinfo r where  u.rev=r.rev ";

    public List<Map<String, Object>> getAllAccounts() {
        List<Map<String, Object>> q = jdbcTemplate.queryForList(ALL_ACCOUNTS);
        return sortData(q);
    }

    /**
     * Method that change revtype to Create account/Edit account/Delete account
     *
     * @param data list of all history flow
     * @return sorted list of all history flow
     * @author Mariia Shchur
     */
    private List<Map<String, Object>> sortData(List<Map<String, Object>> data) {
        for (int i = 0; i < data.size(); i++) {
            for (Map.Entry<String, Object> w : data.get(i).entrySet()) {
                if (w.getKey().equals("revtype") && (w.getValue().equals(0))) {
                    w.setValue("Create account");
                }
                if (w.getKey().equals("revtype") && (w.getValue().equals(1))) {
                    w.setValue("Edit account");
                }
                if (w.getKey().equals("revtype") && (w.getValue().equals(2))) {
                    w.setValue("Delete account");
                }
            }
        }
        return data;
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    final static String FILTER_BY_DATE = "select u.revtype,u.reset_token ,to_timestamp(r.revtstmp/ 1000)::date , u.first_name,u.last_name,u.email,u.enabled,u.phone\n" +
            "from users_aud u ,revinfo r where  u.rev=r.rev and to_timestamp(r.revtstmp/ 1000)::date=(?::date)";
    public List<Map<String, Object>> getAllByData(String date) {
        List<Map<String, Object>> q = jdbcTemplate.queryForList(FILTER_BY_DATE, date);
        return sortData(q);
    }

}
