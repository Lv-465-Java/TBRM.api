package com.softserve.rms.multitenancy;


import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
public class TenantIdResolver implements CurrentTenantIdentifierResolver {
    private String defaultTenant ="tbrm";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String t =  TenantContext.getCurrentTenant();
        if(t!=null){
            return t;
        } else {
            return defaultTenant;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}