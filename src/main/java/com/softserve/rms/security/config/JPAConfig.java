package com.softserve.rms.security.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * JPA Configuration
 *
 * @author Marian Dutchyn
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.softserve.rms.repository")
@EntityScan(basePackages = "com.softserve.rms.entities")
@PropertySource("classpath:application.properties")
public class JPAConfig {
}
