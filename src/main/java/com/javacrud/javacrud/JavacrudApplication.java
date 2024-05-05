package com.javacrud.javacrud;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import com.github.cloudyrock.spring.v5.EnableMongock;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableMongock
@ComponentScan(basePackages = "com.javacrud.javacrud")
public class JavacrudApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavacrudApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Set Spring Boot TimeZone to UTC
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
