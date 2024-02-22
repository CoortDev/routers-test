package com.example.test.routerstest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RoutersTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoutersTestApplication.class, args);
	}

}
