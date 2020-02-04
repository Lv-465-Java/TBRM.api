package com.softserve.rms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.MultipartConfigElement;

@Configuration
@ComponentScan
@EnableSwagger2
@SpringBootApplication
public class RmsApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(RmsApplication.class, args);
    }

}
