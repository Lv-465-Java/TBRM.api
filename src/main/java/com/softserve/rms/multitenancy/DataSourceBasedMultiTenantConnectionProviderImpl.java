package com.softserve.rms.multitenancy;

import com.softserve.rms.constants.DataBases;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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

    private Map<String, DataSource> map = new HashMap<>();

    boolean init = false;

    @PostConstruct
    public void load() {
        map.put(DataBases.DEFAULT_DATABASE, defaultDS);
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return map.get(DataBases.DEFAULT_DATABASE);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        if (!init) {
            TenantDataSource tenantDataSource = context.getBean(TenantDataSource.class);
            init = true;
            map.putAll(tenantDataSource.getAll());

        }
        return map.get(tenantIdentifier) != null ? map.get(tenantIdentifier) : map.get(DataBases.DEFAULT_DATABASE);
    }


}