package com.proriberaapp.ribera;

import com.proriberaapp.ribera.Domain.entities.UserAdminEntity;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class RiberaApplication {

	public static void main(String[] args) {
		SpringApplication.run(RiberaApplication.class, args);
	}

}
