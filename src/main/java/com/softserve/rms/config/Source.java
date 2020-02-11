package com.softserve.rms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class Source {

    @Value("${spring.datasource.url}")
    private String postgresUrl;
    @Value("${spring.datasource.driver-class-name}")
    private String postgresDriver;
    @Value("${spring.datasource.username}")
    private String postgresUser;
    @Value("${spring.datasource.password}")
    private String postgresPassword;

//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource myDataSource() {
//        return DataSourceBuilder
//                .create()
//                .driverClassName("org.postgresql.Driver")
//                .url("${spring.datasource.url}")
//                .username("${spring.datasource.username}")
//                .password("${spring.datasource.password}")
//                .build();

//        PGSimpleDataSource dataSource = new PGSimpleDataSource();
//        dataSource.setDatabaseName("tbrm");
//        dataSource.setServerName("localhost");
//        dataSource.setUser("postgres");
//        dataSource.setPassword("root");
//        return dataSource;

//        Driver sqlDriver = new Driver();
//        return new DataSource(sqlDriver,
//                "jdbc:mysql://localhost:3306/lv326?useSSL=false", "root", "root");

//    }
//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource myDataSource() {
//        return DataSourceBuilder.create().build();
//    }

//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.datasource")
//    public HikariDataSource dataSource() {
//        return DataSourceBuilder.create()
//                .type(HikariDataSource.class)
//                .build();
//    }

    //    spring.datasource.url=jdbc:postgresql://localhost:5432/tbrm
//    spring.datasource.username=postgres
//    spring.datasource.password=root
//    spring.datasource.driver-class-name=org.postgresql.Driver
    @Primary
    @Bean
    public DataSource customDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(postgresDriver);
        dataSource.setUrl(postgresUrl);
        dataSource.setUsername(postgresUser);
        dataSource.setPassword(postgresPassword);
        return dataSource;
    }
}