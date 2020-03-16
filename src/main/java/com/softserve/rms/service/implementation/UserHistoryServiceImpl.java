package com.softserve.rms.service.implementation;

import com.softserve.rms.repository.UserHistoryRepository;
import com.softserve.rms.repository.implementation.UserHistoryRepositoryImpl;
import com.softserve.rms.service.UserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class UserHistoryServiceImpl implements UserHistoryService {

    private UserHistoryRepository userHistoryRepository;
    private String endpointUrl;

    /**
     * Constructor with parameters
     *
     * @author Mariia Shchur
     */
    @Autowired
    public UserHistoryServiceImpl(UserHistoryRepository userHistoryRepository, @Value("${ENDPOINT_URL}") String endpointUrl) {

        this.userHistoryRepository=userHistoryRepository;
        this.endpointUrl = endpointUrl;
    }

    private Object password;
    private Object first_name;
    private Object last_name ;
    private Object phone;
    private Object email;
    private Object reset_token ;
    private Boolean passwordChange = false;

    /**
     * {@inheritDoc}
     *
     * @author Mariia Shchur
     */

    public List<Map<String, Object>> getUserHistory(Long id) {
        List<Map<String, Object>> data =getPhotoUrl(userHistoryRepository.getUserHistory(id));
        for (int i = 0; i < data.size(); i++) {
            Iterator<Map.Entry<String, Object>> iterator = data.get(i).entrySet().iterator();
            if (i == 0) {
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> map = iterator.next();
                    rememberDataWhenCreate(map);
                    if (map.getKey().equals("password") || map.getKey().equals("reset_token")) {
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
                if (map.getKey().equals("revtype") && (map.getValue().equals(2))) {
                    map.setValue("Account has been deleted");
                }
            }
            passwordChange = false;
        }
        return data;
    }

    /**
     * Method that compare previous user's password to current
     * and set message if it was changed
     * or delete row if dont
     *
     * @param iterator
     * @param map      list of history flow
     * @author Mariia Shchur
     */
    private void passwordMessage(Iterator<Map.Entry<String, Object>> iterator,
                                 Map.Entry<String, Object> map) {
        if (map.getKey().equals("password")) {
            if (!(map.getValue().equals(password))) {
                password =  map.getValue();
                map.setValue("Password has been changed");
                passwordChange = true;
            } else {
                iterator.remove();
            }
        }
    }

    /**
     * Method that compare previous user's reset token
     * to current and set message if it was changed
     * or delete row if dont
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
     * flow and set user's friendly message when it was changed and
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
            map.setValue("Account has been created");
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

    public List<Map<String, Object>> getDeletedAccounts() {
        return getPhotoUrl(userHistoryRepository.getDeletedAccounts());
    }

    /**
     * Method that add endpoint url to photos names
     *
     * @param data
     * @author Mariia Shchur
     */
    private List<Map<String, Object>> getPhotoUrl(List<Map<String, Object>> data) {
        for (int i = 0; i < data.size(); i++) {
            for (Map.Entry<String, Object> map : data.get(i).entrySet()) {
                if (map.getKey().equals("image_url")) {
                    map.setValue(endpointUrl + map.getValue());
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

    public List<Map<String, Object>> getAllHistory() {
        return sortData(getPhotoUrl(userHistoryRepository.getAllHistory()));
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
                    w.setValue("Account has been created");
                }
                if (w.getKey().equals("revtype") && (w.getValue().equals(1))) {
                    w.setValue("Account has been edited");
                }
                if (w.getKey().equals("revtype") && (w.getValue().equals(2))) {
                    w.setValue("Account has been deleted");
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

    public List<Map<String, Object>> getAllByData(String date) {
        List<Map<String, Object>> q = getPhotoUrl(userHistoryRepository.getAllByData(date));
        return sortData(q);
    }

}
