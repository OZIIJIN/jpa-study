package me.springstudy.jpastudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "userAuditorAware")
public class JpaStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaStudyApplication.class, args);
	}

}
