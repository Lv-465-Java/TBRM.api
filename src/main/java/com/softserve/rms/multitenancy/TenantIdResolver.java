package com.softserve.rms.multitenancy;


import com.softserve.rms.constants.DataBases;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdResolver implements CurrentTenantIdentifierResolver {


    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant =  TenantContext.getCurrentTenant();
        if(tenant!=null){
            return tenant;
        } else {
            return DataBases.DEFAULT_DATABASE;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}