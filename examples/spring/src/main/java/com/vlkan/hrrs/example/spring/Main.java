package com.vlkan.hrrs.example.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        run(args);
    }

    public static ConfigurableApplicationContext run(String[] args) {
        return SpringApplication.run(Main.class, args);
    }

}
