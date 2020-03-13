package com.softserve.rms.multitenancy;

import com.softserve.rms.constants.DataBaseProperty;
import com.softserve.rms.entities.DataSourceConfig;
import com.softserve.rms.repository.ConfigDao;
import org.hibernate.annotations.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
@Component
public abstract class AbstractDataSourceRouter extends AbstractDataSource {

    @Autowired
    private ConfigDao configDao;

    private Map<Object, DataSource> resolvedDataSources = new HashMap<>();


    @PostConstruct
    public DataSource initDefaultDataSource() {
        DataSource defaultDataSource ;

            DataSourceBuilder factory = DataSourceBuilder.create()
                    .username(DataBaseProperty.USER_NAME)
                    .driverClassName(DataBaseProperty.DRIVER)
                    .password(DataBaseProperty.PASSWORD)
                    .url(DataBaseProperty.URL);
            defaultDataSource = factory.build();
            resolvedDataSources.put(DataBaseProperty.DEFAULT_DATABASE, defaultDataSource);
        return defaultDataSource;
    }

    public void initDataSources() {
        try {
        if (determineCurrentLookupKey() != null) {
            DataSourceConfig config = configDao.findByName(determineCurrentLookupKey(),initDefaultDataSource().getConnection());
            if (resolvedDataSources.get(determineCurrentLookupKey()) == null) {
                DataSourceBuilder factory = DataSourceBuilder
                        .create().driverClassName(config.getDriverclassname())
                        .username(config.getUsername())
                        .password(config.getPassword())
                        .url(config.getUrl());
                DataSource ds = factory.build();
                resolvedDataSources.put(determineCurrentLookupKey(), ds);
            }
        }}catch (SQLException s){
            s.printStackTrace();
        }
    }

    public DataSource determineTargetDataSource() {
        initDataSources();
        Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
        Object lookupKey = determineCurrentLookupKey();
        DataSource dataSource = resolvedDataSources.get(lookupKey);
        if (dataSource == null && lookupKey == null) {


                dataSource = initDefaultDataSource();
                return dataSource;

        }
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
        }
        return  dataSource;
    }
    @Override
    public Connection getConnection() throws SQLException {
        return determineTargetDataSource().getConnection();
    }
    protected abstract String determineCurrentLookupKey();

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineTargetDataSource().getConnection(username,password);
    }
}