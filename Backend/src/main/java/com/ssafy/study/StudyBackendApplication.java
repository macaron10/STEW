package com.ssafy.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyBackendApplication.class, args);
	}

}
