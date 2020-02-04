package com.softserve.rms.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, D> {

    List<T> findAll() ;

    Optional<T> findById(Long id);

    void create(D entity);

    void update(D entity);
}