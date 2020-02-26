package com.softserve.rms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.softserve.rms.*")
public class RmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RmsApplication.class, args);
    }

}
