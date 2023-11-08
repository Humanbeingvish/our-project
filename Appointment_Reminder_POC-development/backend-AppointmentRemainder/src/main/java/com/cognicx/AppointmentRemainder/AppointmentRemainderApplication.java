package com.cognicx.AppointmentRemainder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.cognicx")
@EnableScheduling
public class AppointmentRemainderApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentRemainderApplication.class, args);
	}

}
