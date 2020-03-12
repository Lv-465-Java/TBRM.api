//package com.softserve.rms.controller;
//
//import com.softserve.rms.entities.DataSourceConfig;
//import com.softserve.rms.service.implementation.DataSourceConfigServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class SuperAdminController {
//
//    private final DataSourceConfigServiceImpl dataSourceConfigService;
//
//    @Autowired
//    public SuperAdminController(DataSourceConfigServiceImpl dataSourceConfigService) {
//        this.dataSourceConfigService = dataSourceConfigService;
//    }
//
//    @PostMapping(value = "/superadmin/user")
//    public ResponseEntity<DataSourceConfig> addDataSource(@RequestBody DataSourceConfig newDataSource) {
//        dataSourceConfigService.create(newDataSource);
//        return new ResponseEntity<>(newDataSource, HttpStatus.CREATED);
//    }
//}
