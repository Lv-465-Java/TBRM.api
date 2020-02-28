package com.softserve.rms.service.implementation;

import com.softserve.rms.entities.DataSourceConfig;
import com.softserve.rms.repository.DataSourceConfigRepository;
import com.softserve.rms.service.DataSourceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

@Service
public class DataSourceConfigServiceImpl implements DataSourceConfigService {
    private Properties property = new Properties();
    private FileInputStream fis;
   private FileOutputStream fos;
    private  DataSourceConfigRepository dataSourceConfigRepository;
@Autowired
   public DataSourceConfigServiceImpl (DataSourceConfigRepository dataSourceConfigRepository) {
  this.dataSourceConfigRepository = dataSourceConfigRepository;
}
    @Override
    public void create(DataSourceConfig dataSourceConfig) {
        dataSourceConfigRepository.save(dataSourceConfig);
    }

    public void setDataSource(String name){
       DataSourceConfig dataSourceConfig =  dataSourceConfigRepository.findByName(name);
        try {
            fis = new FileInputStream("C:\\Users\\admin\\Desktop\\recources\\TBRM.api\\src\\main\\resources\\config.properties");
            property.load(fis);
            String url = dataSourceConfig.getUrl().replaceAll("\\\\","");
            property.setProperty("spring.datasource.url",url);
            property.setProperty("spring.datasource.username",dataSourceConfig.getUsername());
            property.setProperty("spring.datasource.password",dataSourceConfig.getPassword());
            property.setProperty("spring.datasource.driver-class-name",dataSourceConfig.getDriverclassname());
            fos = new FileOutputStream("C:\\Users\\admin\\Desktop\\recources\\TBRM.api\\src\\main\\resources\\config.properties");
            property.store(fos,null);


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}
}
