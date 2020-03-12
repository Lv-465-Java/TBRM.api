package com.softserve.rms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BaseService<T, D> {

    Page<D> findAll(Integer page, Integer pageSize);

    Optional<T> findById(Long id);

    void create(D entity);

    void update(D entity);
}