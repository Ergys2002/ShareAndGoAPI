package com.app.ShareAndGo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableScheduling
public class ShareAndGoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareAndGoApplication.class, args);
	}

	/*todo
	*  test implementation for method payForTrip */



}
