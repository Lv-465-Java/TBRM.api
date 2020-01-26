package com.softserve.rms.service;

import com.softserve.rms.entities.ResourceTemplate;

import java.util.List;

public interface ResourceTemplateService {

    ResourceTemplate getTemplateById(Long id);

    List<ResourceTemplate> getAllByUserId(Long id);

    ResourceTemplate createTemplate();

    ResourceTemplate updateTemplateById(Long id);

    void deleteTemplateById(Long id);

//    List<ResourceTemplate> searchTemplateByNameContaining();
}