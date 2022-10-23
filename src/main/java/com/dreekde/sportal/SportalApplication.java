package com.dreekde.sportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SportalApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportalApplication.class, args);
    }

}
