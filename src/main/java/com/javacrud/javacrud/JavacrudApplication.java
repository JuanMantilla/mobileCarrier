package com.javacrud.javacrud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.javacrud.javacrud")
public class JavacrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavacrudApplication.class, args);
	}

}
