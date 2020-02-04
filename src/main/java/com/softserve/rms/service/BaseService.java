package com.softserve.rms.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BaseService<T, D> {

    List<T> findAll() throws SQLException;

    Optional<T> findById(Long id) throws SQLException;

    void create(D entity) throws SQLException;

    void update(D entity) throws SQLException;
}