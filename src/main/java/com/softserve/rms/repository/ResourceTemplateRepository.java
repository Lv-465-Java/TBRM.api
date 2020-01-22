package com.softserve.rms.repository;

import com.softserve.rms.entities.ResourceTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceTemplateRepository extends JpaRepository<ResourceTemplate, Integer> {
    List<ResourceTemplate> findAll();

    ResourceTemplate findById(Long id);
}