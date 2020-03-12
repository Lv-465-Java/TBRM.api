package com.softserve.rms.multitenancy;


import com.softserve.rms.constants.DataBaseProperty;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdResolver implements CurrentTenantIdentifierResolver {


    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant =  TenantContext.getCurrentTenant();
        if(tenant!=null){
            TenantContext.clear();
            return tenant;
        } else {
            return DataBaseProperty.DEFAULT_DATABASE;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}