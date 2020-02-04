package com.softserve.rms.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Configuration for ACL Method Security
 *
 * @author Marian Dutchyn
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AclMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    private MethodSecurityExpressionHandler expressionHandler;

    /**
     * Constructor
     */
    @Autowired
    public AclMethodSecurityConfig(MethodSecurityExpressionHandler expressionHandler) {
        this.expressionHandler = expressionHandler;
    }

    /**
     * Creates an instance of {@link DefaultMethodSecurityExpressionHandler}
     */
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return expressionHandler;
    }
}
