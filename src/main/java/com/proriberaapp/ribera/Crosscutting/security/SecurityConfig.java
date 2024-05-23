package com.proriberaapp.ribera.Crosscutting.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityContextRepository securityContextRepository;

    @Value("${url.version.v1}")
    private String PATH_INIT;
    @Value("${url.admin}")
    private String PATH_ADMIN;
    @Value("${url.manager}")
    private String PATH_MANAGER;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)

                .authorizeExchange(auth -> auth
                        .pathMatchers(OPTIONS).permitAll()
                        .pathMatchers(PATH_ADMIN + "/login").permitAll()

                        .pathMatchers(
                                PATH_INIT + "/users/**",
                                PATH_INIT + "/password-reset/**",
                                "/api/documenttype/**",
                                PATH_INIT + "/payment-books/**",
                                "/swagger-doc/**"
                        ).permitAll()

                        .pathMatchers(PATH_ADMIN + "/**").hasRole("SUPER_ADMIN")

                        .pathMatchers(PATH_MANAGER + "/**").hasAnyRole("ADMIN")
                        .pathMatchers(GET, PATH_MANAGER + "/**").hasAnyAuthority("READ")
                        .pathMatchers(POST, PATH_MANAGER + "/**").hasAnyAuthority("WRITE")
                        .pathMatchers(PATCH, PATH_MANAGER + "/**").hasAnyAuthority("UPDATE")
                        .pathMatchers(DELETE, PATH_MANAGER + "/**").hasAnyAuthority("DELETE")

                        .anyExchange().authenticated()
                )

                .securityContextRepository(securityContextRepository)

                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)

                .build();
    }

}