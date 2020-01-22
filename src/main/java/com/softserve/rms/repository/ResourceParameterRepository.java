package com.softserve.rms.repository;

import com.softserve.rms.entities.ResourceParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceParameterRepository extends JpaRepository<ResourceParameter, Integer> {

    ResourceParameter findById(Long id);
}