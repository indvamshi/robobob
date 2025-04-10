package com.maths.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Spring Boot application.
 * This class uses the {@link SpringBootApplication} annotation to enable auto-configuration,
 * component scanning, and Spring Boot's features.
 */
@SpringBootApplication
public class Application {

	/**
	 * The main method that starts the Spring Boot application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
