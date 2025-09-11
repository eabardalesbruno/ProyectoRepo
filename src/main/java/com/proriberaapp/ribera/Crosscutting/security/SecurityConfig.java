package com.proriberaapp.ribera.Crosscutting.security;

import lombok.RequiredArgsConstructor;
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

        @Bean
        public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

                return http
                                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                                .cors(ServerHttpSecurity.CorsSpec::disable)
                                .authorizeExchange(auth -> auth
                                                .pathMatchers(OPTIONS).permitAll()
                                                .pathMatchers(
                                                                "/api/v1/socios/**",
                                                                "/api/v1/beneficiarios/**",
                                                                "/api/v1/membresias/**",
                                                                "/api/v1/socios/page",
                                                                "/api/v1/socios/search",
                                                                "/api/v1/beneficiarios",
                                                                "/api/v1/beneficiarios/filter",
                                                                "/api/v1/beneficiarios/{id}",
                                                                "/api/v1/beneficiarios/{id}/checkin",
                                                                "/api/v1/beneficiarios/byMembership/{id}",
                                                                "/api/v1/membresias/user/{idUser}")
                                                .permitAll()
                                                .anyExchange().permitAll())
                                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                                .logout(ServerHttpSecurity.LogoutSpec::disable)
                                .build();
        }

}