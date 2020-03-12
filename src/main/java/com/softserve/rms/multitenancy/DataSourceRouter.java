package com.softserve.rms.multitenancy;

import org.springframework.stereotype.Component;

@Component
public class DataSourceRouter extends AbstractDataSourceRouter {

    @Override
    public String determineCurrentLookupKey() {
        return TenantContext.getCurrentTenant();
    }
}
