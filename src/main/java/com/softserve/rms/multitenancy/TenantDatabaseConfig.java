package com.softserve.rms.multitenancy;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import com.softserve.rms.entities.User;
import com.softserve.rms.repository.UserRepository;
import com.softserve.rms.service.UserService;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.softserve.rms.repository",
        "com.softserve.rms.entities"})
@EnableJpaRepositories(basePackages = {
        "com.softserve.rms.repository",
        "com.softserve.rms.service"},
        entityManagerFactoryRef = "tenantEntityManagerFactory",
        transactionManagerRef = "tenantTransactionManager")
public class TenantDatabaseConfig {

    private static final Logger LOG = LoggerFactory
            .getLogger(TenantDatabaseConfig.class);


    @Bean(name = "tenantJpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean(name = "tenantTransactionManager")
    public JpaTransactionManager transactionManager(
            EntityManagerFactory tenantEntityManager) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tenantEntityManager);
        return transactionManager;
    }

    @Bean(name = "datasourceBasedMultitenantConnectionProvider")
    @ConditionalOnBean(name = "masterEntityManagerFactory")
    public MultiTenantConnectionProvider multiTenantConnectionProvider() {
        return new DataSourceBasedMultiTenantConnectionProviderImpl();
    }

    @Bean(name = "currentTenantIdentifierResolver")
    public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
        return new TenantIdResolver();
    }

    @Bean(name = "tenantEntityManagerFactory")
    @ConditionalOnBean(name = "datasourceBasedMultitenantConnectionProvider")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("datasourceBasedMultitenantConnectionProvider")
                    MultiTenantConnectionProvider connectionProvider,
            @Qualifier("currentTenantIdentifierResolver")
                    CurrentTenantIdentifierResolver tenantResolver) {
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setPackagesToScan(
                new String[]{User.class.getPackage().getName(),
                        UserRepository.class.getPackage().getName(),
                        UserService.class.getPackage().getName()});
        emfBean.setJpaVendorAdapter(jpaVendorAdapter());
        emfBean.setPersistenceUnitName("tenantdb-persistence-unit");
        Map<String, Object> properties = new HashMap<>();
        properties.put(org.hibernate.cfg.Environment.MULTI_TENANT,
                MultiTenancyStrategy.SCHEMA);
        properties.put(
                org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER,
                connectionProvider);
        properties.put(
                org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,
                tenantResolver);
        properties.put(org.hibernate.cfg.Environment.DIALECT,
                "org.hibernate.dialect.MySQL5Dialect");
        properties.put(org.hibernate.cfg.Environment.SHOW_SQL, true);
        properties.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "update");
        emfBean.setJpaPropertyMap(properties);
        LOG.info("tenantEntityManagerFactory set up successfully!");
        return emfBean;
    }
}