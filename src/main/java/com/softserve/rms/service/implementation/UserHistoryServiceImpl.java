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


    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    final static String SQL = "select u.revtype,u.first_name,u.last_name,u.email,u.enabled,u.phone, to_timestamp(r.revtstmp/ 1000)  from users_aud u ,revinfo r where  u.rev=r.rev and u.id = ? ";

    public List<Map<String, Object>> getUserHistory(Long id) {
        List<Map<String, Object>> q = change_password(id);
        return q;
    }

    final static String EDIT_DATA = "select u.revtype,to_timestamp(r.revtstmp/ 1000)::timestamp,u.first_name, u.last_name, u.email, u.phone,u.password,\n" +
            "u.reset_token from users_aud u ,revinfo r where  \n" +
            "u.rev=r.rev and u.id = ?";

    private List<Map<String, Object>> change_password(long id) {
        List<Map<String, Object>> data = jdbcTemplate.queryForList(EDIT_DATA, id);
        String password = null;
        String first_name = null;
        String last_name = null;
        String phone = null;
        String email = null;
        String reset_token = null;
        Boolean passwordChange=false;
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                for (Map.Entry<String, Object> w : data.get(i).entrySet()) {
                    if (w.getKey().equals("revtype") && (w.getValue().equals(0))) {
                        w.setValue("Create user");
                    }
                    if (w.getKey().equals("password")) {
                        password = (String) w.getValue();
                    }
                    if (w.getKey().equals("email")) {
                        email = (String) w.getValue();
                    }
                    if (w.getKey().equals("phone")) {
                        phone = (String) w.getValue();
                    }
                    if (w.getKey().equals("first_name")) {
                        first_name = (String) w.getValue();
                    }
                    if (w.getKey().equals("last_name")) {
                        last_name = (String) w.getValue();
                    }
                }
                continue;
            }
            Iterator<Map.Entry<String, Object>> q = data.get(i).entrySet().iterator();
            while (q.hasNext()) {
                Map.Entry<String,Object> w=q.next();
                if (w.getKey().equals("revtype") && (w.getValue().equals(1))){
                    q.remove();
                }
                if (w.getKey().equals("password")) {
                    if (!(w.getValue().equals(password))) {
                        password = (String) w.getValue();
                        w.setValue("Password has been changed");
                        passwordChange=true;
                    } else {
                       q.remove();
                    }
                }

                if (w.getKey().equals("phone")) {
                    if (!(w.getValue().equals(phone))) {
                        phone = (String) w.getValue();
                        w.setValue("phone has been changed - " + phone);
                    } else {
                        q.remove();
                    }
                }
                if (w.getKey().equals("email")) {
                    if (!(w.getValue().equals(email))) {
                        email = (String) w.getValue();
                        w.setValue("email has been changed - " + email);
                    } else {
                        q.remove();
                    }
                }
                if (w.getKey().equals("first_name")) {
                    if (!(w.getValue().equals(first_name))) {
                        first_name = (String) w.getValue();
                        w.setValue("first_name has been changed - " + first_name);
                    } else {
                        q.remove();
                    }
                }
                if (w.getKey().equals("last_name")) {
                    if (!(w.getValue().equals(last_name))) {
                        last_name = (String) w.getValue();
                        w.setValue("last_name has been changed - " + last_name);
                    } else {
                        q.remove();
                    }
                }
                if (w.getKey().equals("reset_token")) {
                    if (w.getValue() != reset_token) {
                        reset_token = (String) w.getValue();
                        if(passwordChange){
                            w.setValue("password was reseted by link from email");
                        }else w.setValue("Link for password reseting was send om email");
                    } else {
                        q.remove();
                    }
                }
            }
            passwordChange=false;
        }
        return data;
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    final static String DELETED_ACCOUNTS = "select to_timestamp(r.revtstmp/ 1000)::timestamp  as time,u.first_name,u.last_name, u.email, u.phone from users_aud u,\n" +
            "revinfo r where u.revtype=0 \n" +
            "and u.id in (\n" +
            "\tselect u.id from users_aud u, revinfo r where u.revtype=2 \n" +
            ") and r.revtstmp in (\n" +
            "\tselect r.revtstmp from users_aud u, revinfo r where u.revtype=2 and u.rev=r.rev)";

    public List<Map<String, Object>> getDeletedAccounts() {
        List<Map<String, Object>> q = jdbcTemplate.queryForList(DELETED_ACCOUNTS);
        return q;
    }

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */
    final static String ALL_ACCOUNTS = "select u.revtype,u.first_name,u.last_name,u.email,u.enabled,u.phone, to_timestamp(r.revtstmp/ 1000)  from users_aud u ,revinfo r where  u.rev=r.rev ";

    public List<Map<String, Object>> getAllAccounts() {
        List<Map<String, Object>> q = jdbcTemplate.queryForList(ALL_ACCOUNTS);
        sortData(q);
        return q;
    }

    private List<Map<String, Object>> sortData(List<Map<String, Object>> data) {
        for (int i = 0; i < data.size(); i++) {
            for (Map.Entry<String, Object> w : data.get(i).entrySet()) {
                if (w.getKey().equals("revtype") && (w.getValue().equals(0))) {
                    w.setValue("Create user");
                }
                if (w.getKey().equals("revtype") && (w.getValue().equals(1))) {
                    w.setValue("Edit date");
                }
                if (w.getKey().equals("revtype") && (w.getValue().equals(2))) {
                    w.setValue("Delete user");
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
    final static String FILTER_BY_DATE = "select u.reset_token , u.revtype, to_timestamp(r.revtstmp/ 1000)::date , u.first_name,u.last_name,u.email,u.enabled,u.phone\n" +
            "from users_aud u ,revinfo r where  u.rev=r.rev and to_timestamp(r.revtstmp/ 1000)::date=(?::date)";

    public List<Map<String, Object>> getAllByData(String date) {
        List<Map<String, Object>> q = jdbcTemplate.queryForList(FILTER_BY_DATE, date);
        sortData(q);
        return q;
    }

}
