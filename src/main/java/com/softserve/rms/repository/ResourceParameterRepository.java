package com.softserve.rms.repository;

import com.softserve.rms.entities.ResourceParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceParameterRepository extends JpaRepository<ResourceParameter, Long> {

    List<ResourceParameter> findAllByResourceTemplateId(Long id);
}