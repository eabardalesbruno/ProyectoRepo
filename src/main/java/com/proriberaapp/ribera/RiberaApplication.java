package com.proriberaapp.ribera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
@ComponentScan(basePackages = {"com.proriberaapp.ribera.Infraestructure.repository"})
public class RiberaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiberaApplication.class, args);
	}

}
