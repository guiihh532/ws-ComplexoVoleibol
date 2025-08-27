package com.ex.complexovoleibol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class ComplexovoleibolApplication {
    public static void main(String[] args) {
        SpringApplication.run(ComplexovoleibolApplication.class, args);
    }
}