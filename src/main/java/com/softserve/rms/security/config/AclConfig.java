package com.softserve.rms.security.config;

import com.softserve.rms.constants.AclQueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class AclConfig {

    private final DataSource dataSource;

    @Autowired
    public AclConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public MutableAclService aclService() {
        JdbcMutableAclService jdbcMutableAclService =
                new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());

        jdbcMutableAclService.setClassIdentityQuery(AclQueries.CLASS_IDENTITY_QUERY);
        jdbcMutableAclService.setSidIdentityQuery(AclQueries.SID_IDENTITY_QUERY);

        jdbcMutableAclService.setObjectIdentityPrimaryKeyQuery(AclQueries.OBJECT_IDENTITY_PRIMARY_KEY_QUERY);
        jdbcMutableAclService.setFindChildrenQuery(AclQueries.FIND_CHILDREN_QUERY);

        return jdbcMutableAclService;
    }

    @Bean
    public EhCacheManagerFactoryBean aclCacheManager() {
        return new EhCacheManagerFactoryBean();
    }

    @Bean
    public EhCacheFactoryBean aclEhCacheFactoryBean() {
        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject());
        ehCacheFactoryBean.setCacheName("aclCache");
        return ehCacheFactoryBean;
    }

    @Bean
    public EhCacheBasedAclCache aclCache() {
        return new EhCacheBasedAclCache(
                aclEhCacheFactoryBean().getObject(),
                permissionGrantingStrategy(),
                aclAuthorizationStrategy()
        );
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(() -> "ROLE_MANAGER");
    }

    @Bean
    public LookupStrategy lookupStrategy() {
        return new BasicLookupStrategy(dataSource,
                aclCache(),
                aclAuthorizationStrategy(),
                new ConsoleAuditLogger());
    }


    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        AclPermissionEvaluator aclPermissionEvaluator = new AclPermissionEvaluator(aclService());
        expressionHandler.setPermissionEvaluator(aclPermissionEvaluator);
        return expressionHandler;
    }
}
