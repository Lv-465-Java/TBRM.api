package com.softserve.rms.multitenancy;

import com.softserve.rms.entities.DataSourceConfig;
import com.softserve.rms.service.implementation.DataSourceConfigServiceImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    @Autowired
    private DataSource defaultDS;
    @Autowired
    private ApplicationContext context;
    @Autowired
    @Lazy
    private DataSourceConfigServiceImpl dataSourceConfigService;

    private Map<String, DataSource> map = new HashMap<>();

    boolean init = false;

    @PostConstruct
    public void load() {
        map.put("tbrm", defaultDS);
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return map.get("tbrm");
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        tenantIdentifier = TenantContext.getCurrentTenant();
        if(tenantIdentifier == null){
            tenantIdentifier = "tbrm";
        }

        if (!init) {
            TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
            init = true;
            map.putAll(tenantDataSource.getAll());

        }
        if (map.get(tenantIdentifier) == null) {
            map.put(tenantIdentifier,createDataSource(tenantIdentifier));
        }
        return map.get(tenantIdentifier) != null ? map.get(tenantIdentifier) : map.get("tbrm");
    }

    private DataSource createDataSource(String name) {
        DataSourceConfig config = dataSourceConfigService.findByName(name);
        if (config != null) {
            DataSourceBuilder factory = DataSourceBuilder
                    .create().driverClassName(config.getDriverclassname())
                    .username(config.getUsername())
                    .password(config.getPassword())
                    .url(config.getUrl());
            DataSource ds = factory.build();
            return ds;
        }
        return null;
    }

}