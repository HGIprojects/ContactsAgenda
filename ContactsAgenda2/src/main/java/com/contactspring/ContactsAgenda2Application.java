package com.contactspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ContactsAgenda2Application {

	public static void main(String[] args) {
		SpringApplication.run(ContactsAgenda2Application.class, args);
	}

}
