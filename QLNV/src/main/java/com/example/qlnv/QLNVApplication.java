package com.example.qlnv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.qlnv.repository")
@EntityScan(basePackages = "com.example.qlnv.model")

public class QLNVApplication {
	public static void main(String[] args) {
		SpringApplication.run(QLNVApplication.class, args);
	}
}