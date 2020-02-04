package com.softserve.rms.service;

import com.softserve.rms.entities.User;

/**
 * Provides the interface to manage {@link User} entity.
 * @author Kravets Maryana
 */
public interface UserService {

    /**
     * Method that allow you to get {@link User} by email.
     *
     * @param email a value of {@link String}
     * @return {@link User}
     */
    User getUserByEmail(String email);

    /**
     * Method that allow you to get {@link User} by ID.
     *
     * @param id a value of {@link Long}
     * @return {@link User}
     */
    User getById(long id);
}
