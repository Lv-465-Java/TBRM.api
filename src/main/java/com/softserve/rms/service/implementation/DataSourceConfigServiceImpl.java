package com.softserve.rms.service.implementation;

import com.softserve.rms.entities.DataSourceConfig;
import com.softserve.rms.repository.DataSourceConfigRepository;
import com.softserve.rms.service.DataSourceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataSourceConfigServiceImpl implements DataSourceConfigService {

    private  DataSourceConfigRepository dataSourceConfigRepository;
@Autowired
   public DataSourceConfigServiceImpl (DataSourceConfigRepository dataSourceConfigRepository){
        this.dataSourceConfigRepository = dataSourceConfigRepository;
    }
    @Override
    public void create(DataSourceConfig dataSourceConfig) {
        dataSourceConfigRepository.save(dataSourceConfig);
    }

    public DataSourceConfig findByName(String name){
        return dataSourceConfigRepository.findByName(name);
    }
}
