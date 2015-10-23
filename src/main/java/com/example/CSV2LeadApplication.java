package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CSV2LeadApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(CSV2LeadApplication.class);
    	app.setWebEnvironment(false);
        app.run(args);
    }
}
