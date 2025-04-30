package com.kaankarakas.librarymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LibrarymanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibrarymanagementApplication.class, args);
	}

}
