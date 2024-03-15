package com.proriberaapp.ribera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class RiberaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiberaApplication.class, args);
	}

}
