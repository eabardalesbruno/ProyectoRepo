package com.proriberaapp.ribera.Crosscutting.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "PROYECTO RIBERA CLUB",
                version = "1.0",
                description = "Creada para la administracion del resort Ribera",
                contact = @Contact(
                        name = "InClub"
                )
        ),
        servers = {
                @Server(
                        description = "DEV LOCAL SERVER",
                        url = "http://localhost:8777"
                ),
                @Server(
                        description = "DEV TEST SERVER",
                        url = "https://riberams-dev.inclub.world"
                )
        },
        security = @SecurityRequirement(
                name = "JWT Token"
        )
)
@SecurityScheme(
        name = "JWT Token",
        description = "Access Token",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
}
