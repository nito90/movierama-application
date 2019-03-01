package com.toulios.projects.movierama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MovieramaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieramaApplication.class, args);
	}

}
