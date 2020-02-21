package com.softserve.rms.config;

import com.softserve.rms.repository.implementation.JooqDDL;
import org.jooq.DSLContext;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Autowired
    private DSLContext dslContext;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public JooqDDL createJooqDDL() {
        return new JooqDDL(dslContext);
    }
}